package test.task.ao1;

import java.io.*;

public class FileReader implements Runnable {

    final private String path;
    final private OutputCache outputCache;

    public FileReader(String path, OutputCache outputCache) {
        this.path = path;
        this.outputCache = outputCache;
    }

    @Override
    public void run() {
        System.out.println("run: " + path);

        try {
            InputStream inputStream = new FileInputStream(new File(path));
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            br.lines()
                    .skip(1) // пропустить заголовок
                    .map(this::mapStringToItem) // получить строку
                    .forEach(this::saveItemToCache); // сохранить в кэш
        } catch (Exception e) {
            System.out.println("error loading: " + path);
            e.printStackTrace();
        }
    }

    private DataItem mapStringToItem(String s) {
        String[] p = s.split(",");
        // TODO проверка значений
        return new DataItem(Integer.valueOf(p[0]), p[1], p[2], p[3], Double.valueOf(p[4]));
    }

    private void saveItemToCache(DataItem dataItem) {
        outputCache.saveItem(dataItem);
    }
}
