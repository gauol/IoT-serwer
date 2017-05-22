import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Gaua on 19.04.2017.
 */

public class Server {
    private static int printetChart = 0;
    private static JDB jdb;
    private static ArrayList<String> tabele;

    public static void main(String[] args) {
        try {
            jdb = new JDB();
            runSerwer();
        }catch (Exception sql){
            System.out.println("FATAL ERROR");
            System.out.println(sql.toString());
        }
    }

    public static ByteBuffer getHtmlBuffer(String path, SocketChannel dos) throws IOException {
        tabele = jdb.listTablesToArray();
        if (path.equals("/"))
            path = "/index.html";
        if(path.charAt(1)=='?') {
            printetChart = Integer.parseInt(path.replaceAll("[\\D]", ""));
            System.out.println("Wykres do wyswietlenia: "+ printetChart);
            path = "/index.html";
        }
        try{
            Path pathF = Paths.get(System.getProperty("user.dir")+"/src/web" + path);
            byte bytes[] = Files.readAllBytes(pathF);
            String response = new String(bytes, "UTF-8");

            if(path.equals("/index.html")){ //wklejam dane z bazy
                String nazwaWykresu;
                int index;
                if(printetChart == 0){
                    nazwaWykresu = "Wybierz wykres z menu";
                }else {
                    nazwaWykresu = "Wykres :" + printetChart;

                    index = response.indexOf("<!-- TuWklejPomiary -->"); //     ",\r\n['" + data + ", " + temp + "]"
                    try {
                        String daneZBazy = jdb.getDataHTML(tabele.get(printetChart - 1));
                        response = new StringBuilder(response).insert(index, daneZBazy).toString();
                    }catch (IndexOutOfBoundsException ignored){}

                }
                index = response.indexOf("<!-- TuWklejSensory -->");
                response = new StringBuilder(response).insert(index, getTablesListHTTP()).toString();
                index = response.indexOf("<!-- TuWklejNazweWykresu -->");
                response = new StringBuilder(response).insert(index, nazwaWykresu).toString();
            }

            //dos.write("hello");
            CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
            //ByteBuffer out = encoder.encode(CharBuffer.wrap(str));
            //
            dos.write(encoder.encode(CharBuffer.wrap("HTTP/1.1 200 OK\r\nContent-length="+response.length())));
            if(path.equals("/index.html"))
                dos.write(encoder.encode(CharBuffer.wrap("\r\nContent-type:text/html; charset=UTF-8\r\n\r\n")));
            else
                dos.write(encoder.encode(CharBuffer.wrap("\r\n\r\n")));

            dos.write(encoder.encode(CharBuffer.wrap(response)));

            return null;
        }
        catch (IOException error){
            System.out.println(error);
            String str = "HTTP/1.1 200 OK\n" +
                    "\n <h1>404</h1>";
            CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
            return encoder.encode(CharBuffer.wrap(str));
        }
    }

    private static void runSerwer() {
        try {
            int port = 90;

            SocketAddress localport = new InetSocketAddress(port);
            ServerSocketChannel tcpserver = ServerSocketChannel.open();
            tcpserver.socket().bind(new InetSocketAddress("localhost", port));
            DatagramChannel udpserver = DatagramChannel.open();

            udpserver.socket().bind(localport);
            ByteBuffer bufferTCP = ByteBuffer.allocate(1024);

            tcpserver.configureBlocking(false);
            udpserver.configureBlocking(false);

            Selector selector = Selector.open();

            tcpserver.register(selector, SelectionKey.OP_ACCEPT);
            udpserver.register(selector, SelectionKey.OP_READ);

            ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
            System.out.println("http://localhost:" + port);
            for (; ; )
                try {
                    selector.select();
                    Set keys = selector.selectedKeys();
                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        SelectionKey key = (SelectionKey) i.next();
                        Channel c = key.channel();

                        if (key.isAcceptable() && c == tcpserver) {
                            SocketChannel client = tcpserver.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                        }
                        else if (key.isReadable() && c == udpserver) {
                            SocketAddress clientAddress = udpserver.receive(receiveBuffer);
                            receiveBuffer.position(0);
                            printRln("Separator");
                            interpreterUDP(clientAddress.toString(), new String( receiveBuffer.array(),"UTF-8"));
                        }else if (key.isReadable()) {
                            printRln("Separator");
                            SocketChannel client = (SocketChannel) key.channel();
                            client.read(bufferTCP);
                            String output = new String(bufferTCP.array()).trim();
                            bufferTCP.clear();
                            if (client != null) {
                                String httpqr = getHTTPquery(output);
                               //uniknac dodawania stringa plusem//
                                Server.getHtmlBuffer(httpqr, client);
                            }
                            client.close();
                        }
                        i.remove();
                    }
                } catch (java.io.IOException e) {
                    Logger l = Logger.getLogger(DaytimeServer.class.getName());
                    l.log(Level.WARNING, "IOException in Server", e);
                } catch (Throwable t) {
                    Logger l = Logger.getLogger(DaytimeServer.class.getName());
                    l.log(Level.SEVERE, "FATAL error in Server", t);
                    System.exit(1);
                }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    static void printRln(String msg){
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }

    private static String getHTTPquery(String daneIN){
        String strTrim = daneIN.substring(daneIN.indexOf("GET")+4, daneIN.length());
        String astrTrim = strTrim.substring(0, strTrim.indexOf(" "));
        System.out.println(astrTrim);
        return astrTrim;
    }
    private static String getTablesListHTTP(){
        StringBuilder listaTabelHTML = new StringBuilder();
        int ctr = 1;
        for(String str : tabele )
            listaTabelHTML.append("<a class=\"w3-bar-item w3-button\" href=\"?id=").append(ctr++).append("\">").append(str).append("</a>\r\n");
        return listaTabelHTML.toString();
    }

    private static void interpreterUDP(String from, String data){
        String inte = data;
        Pattern p = Pattern.compile("(SEN=)(\\D+):(TEMP1=)(\\d+.\\d+):(TEMP2=)(\\d+.\\d+)");
        Matcher m = p.matcher(inte);
        if(m.find()) {
            String id = m.group(2);
            String temp1 = m.group(4);
            String temp2 = m.group(6);
            System.out.println("Odebrano Dane : SEN = " + id + " : " + Float.parseFloat(temp1) + " : " + Float.parseFloat(temp2)+" od: " + from );
        }
    }
}
