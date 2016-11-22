import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpDownloader {

    private int allTime;
    private long totalRead = 0;
    private static OkHttpClient client = new OkHttpClient();;

    public HttpDownloader(String filePath, String url, int byteLimit) throws IOException, InterruptedException {

        FileOutputStream file = new FileOutputStream(filePath);
        download(run(url, client), file, byteLimit);

    }

    private InputStream run(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    private void download(InputStream in, OutputStream out,  int bytes)
    {
        byte[] buffer = new byte[1];
        int read;
        int bufferRead = 0;
        long time = System.currentTimeMillis();
        try {
            while((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
                bufferRead += read;
                totalRead += read;
                if(bufferRead >= bytes)
                {
                    bufferRead = 0;
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        allTime = (int) ((System.currentTimeMillis()-time)/1000);
    }

    public int getTime(){
        return allTime;
    }

    public long getTotalRead(){
        return totalRead;
    }

}
