package test.task.ao1;

import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;

public class OutputCache {

    private final TreeSet<DataItem> cache;
    private final HashMap<Integer, Integer> counters;

    public OutputCache() {
        cache = new TreeSet<>(this::compareItems);
        counters = new HashMap<>();
    }

    private int compareItems(DataItem e1, DataItem e2) {
        if (e1.getPrice() < e2.getPrice()) return -1;
        if (e1.getPrice() > e2.getPrice()) return 1;

        return e1.getName().compareTo(e2.getName());
    }


    public synchronized void saveItem(DataItem item) {
        int count = counters.getOrDefault(item.getProductID(), 0);
        cache.add(item);
        counters.put(item.getProductID(), ++count);

        if (count > 20) removeLastItemByProductID(item.getProductID());
        if (cache.size() > 1000) removeLastItem();
    }

    private void removeLastItem() {
        int count = counters.getOrDefault(cache.last().getProductID(), 0);
        counters.put(cache.last().getProductID(), --count);

        cache.remove(cache.last());
    }

    private void removeLastItemByProductID(int productID) {
        int count = counters.getOrDefault(cache.last().getProductID(), 0);
        counters.put(cache.last().getProductID(), --count);

        cache.stream()
                .filter(dataItem -> dataItem.getProductID() == productID)
                .skip(20)
                .findAny()
                .ifPresent(cache::remove);
    }


    public void saveOutput(String outFileName) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(outFileName));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        cache.forEach(dataItem -> {
            try {
                bw.write(dataItem.toCsvRow());
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bw.close();
    }
}
