package test.task.ao1;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {

    private static void showUsage() {
        System.out.println("Usage:");
        System.out.println("ao1-test-task -dir /some-dir -out some-file");
    }

    public static void main(String[] args) {

        String inputPath = "";
        String outFileName = "";

        if (args.length != 4) {
            showUsage();
            return;
        }

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-dir"))
                inputPath = args[i + 1];
            if (args[i].equals("-out"))
                outFileName = args[i + 1];
        }

        if (inputPath.isEmpty() || outFileName.isEmpty()) {
            showUsage();
            return;
        }

        final OutputCache outputCache = new OutputCache();

        System.out.println("availableProcessors = " + Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newWorkStealingPool();

        int maxDepth = 5;
        try (Stream<Path> stream = Files.find(Paths.get(inputPath), maxDepth, (path, attr) -> String.valueOf(path).endsWith(".csv"))) {
            stream.forEach(path -> {
                System.out.println("start executor: " + path);
                executor.submit(new FileReader(String.valueOf(path), outputCache));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            outputCache.saveOutput(outFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
