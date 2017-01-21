import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PingUrl {

	private URL url;
	HttpURLConnection urlConn;
	long startTime;
	long endTime;

	
	
	PingUrl (final String channel, final String address) {
		try {
			
			url = new URL(address);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	public void pingUrl(final String channel, final String address, int value) {
		try {

			
			startTime = System.currentTimeMillis();
			urlConn.connect();
			endTime = System.currentTimeMillis();
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("Time (ms) : " + (endTime - startTime));
				System.out.println("Ping to " + address + " was success");

				// return true;
			} else {
				System.out.println("Time (ms) : " + (endTime - startTime));
				System.out.println("Ping to " + address + " not success");

				// return false;
			}
		}

		catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		catch (IOException e2) {
			e2.printStackTrace();
		}

	}

}
