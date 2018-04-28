package org.demoTcpServer.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.demoCommons.SpeedMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TcpBioClient {
	private static Logger logger = LoggerFactory.getLogger(TcpBioClient.class);
	private static int port = 7879;
	
	public static void send(String msg){
		DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket = new Socket();
        try {
        	socket.connect(new InetSocketAddress("127.0.0.1", port),30*1000);
        	socket.setReuseAddress(true);
        	socket.setSoTimeout(30*1000);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = msg.getBytes("gbk");
            out.writeInt(bytes.length);
            out.write(bytes);
            out.flush();
            socket.shutdownOutput();
            SpeedMetric.handleRequest("BioClient");
            int len = in.readInt();
            byte[] b = new byte[len];
            in.readFully(b);
            StringBuffer buf = new StringBuffer();
            buf.append(new String(b,0,len,"gbk"));
            System.out.println("client 收到："+buf);
            logger.info("client 收到：{}",buf);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
    public static void main(String[] args) {
    	String msg = "杨五  18511015637 513334199405138168GZYH20171023150544078094";
    	if (args!=null && args.length >=1) {
			port = Integer.parseInt(args[0]);
		}
    	if (args!=null && args.length >=2) {
    		msg = args[1];
    	}
    	int i = 0;
    	long t0 = System.currentTimeMillis();
    	while (++i<=10) {
			send(msg);
		}
    	SpeedMetric.stopHandleRequest("BioClient");
    	long t1 = System.currentTimeMillis();
    	System.out.println("发送完毕，耗时："+(t1 - t0));
    }
}













