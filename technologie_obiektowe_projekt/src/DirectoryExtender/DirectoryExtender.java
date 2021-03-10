package DirectoryExtender;

import java.io.File;

public class DirectoryExtender {
    public static int countFilesInDirectory(String url){
        try {
            return new File(url).list().length;
        }catch (Exception e){
            return 0;
        }

    }
}
