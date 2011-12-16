package client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {

	private ServerSocket server;

	public FileServer() {
		try {
			server = new ServerSocket(9200);
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				InputStream input = client.getInputStream();
				FileOutputStream out = null;
				byte b[] = new byte[1024];
				String fileName = "";
				boolean fileNameFind = false;
				int noofbytesread = 0;
				while ((noofbytesread = input.read(b)) != -1) {
					String data = new String(b);
					if (!fileNameFind) {
						fileName = data.substring(0, data.indexOf("|"));
						out = new FileOutputStream("files/" + fileName);
						fileNameFind = true;
						continue;
					}

					out.write(b, 0, noofbytesread);
				}
				out.close();
				input.close();
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
