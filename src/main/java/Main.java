import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private static int countThreads = 0;
    private static int byteLimitPerSec = 0;
    private static String pathOnListOfLinks = "";
    private static String pathWhereDownloadFiles = "";
    private static int countDownloadBytes;
    private static long time;

    public static void main(String[] args) {

        time = System.currentTimeMillis();

        for (int i=0; i<args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-n")) {
                countThreads = Integer.parseInt(args[++i]);
            } else if (arg.equals("-l")) {
                String string = args[++i];
                if (string.substring(string.length()-1, string.length()).equals("k")){
                    byteLimitPerSec = Integer.parseInt(string.substring(0, string.length()-1))*1024;
                }else{
                    byteLimitPerSec = Integer.parseInt(string);
                }
            } else if (arg.equals("-f")) {
                pathOnListOfLinks = args[++i];
            } else if (arg.equals("-o")) {
                pathWhereDownloadFiles = args[++i];
            }
        }

        if (countThreads == 0){
            System.out.println("Enter thread count");
        }if (byteLimitPerSec == 0){
            System.out.println("Enter byte per second count");
        }if (pathOnListOfLinks.equals("")){
            System.out.println("Enter path to file with urls");
        }if (pathWhereDownloadFiles.equals("")){
            System.out.println("Enter the name of the folder where to put downloaded files");
        }

        FileSplitter fileSplitter = new FileSplitter(pathOnListOfLinks);
        File file = new File(pathWhereDownloadFiles);
        file.mkdirs();

        ExecutorService service = Executors.newFixedThreadPool(countThreads);
        for (int i = 0; i < fileSplitter.getPair().size(); i++){
            final int finalI = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpDownloader downloader = new HttpDownloader(file.toString()+"/"
                                                    + fileSplitter.getPair().get(finalI).getSecond()
                                                    ,fileSplitter.getPair().get(finalI).getFirst()
                                                    ,byteLimitPerSec);

                        countDownloadBytes += downloader.getTotalRead();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        service.shutdown();
        while (!service.isTerminated()){
        }
        System.out.println("Download byte: "+countDownloadBytes);
        System.out.println("Past time: "+((System.currentTimeMillis()-time)/1000));
    }

}