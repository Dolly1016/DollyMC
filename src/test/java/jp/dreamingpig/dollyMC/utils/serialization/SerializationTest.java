package jp.dreamingpig.dollyMC.utils.serialization;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTest {

    private static final Logger logger = LoggerFactory.getLogger(SerializationTest.class);


    /**
     * データの永続化が使用できます。
     */
    @Test
    void serializationWorks() {
        // 実際の環境では DStructureEntry.openStructure(plugin, filename) で永続化されたデータ構造を得られます。
        DebugSerialization serialization = new DebugSerialization();
        var structure = DStructureEntry.debugStructure(serialization);

        // データ構造に整数型のデータentryを追加します。デフォルト値は10です。
        var entry = structure.getInt("entry", 10);

        //値を変更せずに取得すると、デフォルト値が得られます。
        assertEquals(10, entry.get(), "デフォルト値が得られる必要があります。");

        //値を変更したのちに値を取得すると、変更後の値が得られます。
        entry.set(50);
        assertEquals(50, entry.get(), "変更後の値が得られる必要があります。");

        //実際の環境では、ファイルにこのテキストが書き込まれ、データが永続化されます。
        assertEquals("entry: 50\n", serialization.toString(), "データがテキストに正しく出力される必要があります。");
    }

    @Test
    void serializationStructureList(){
        // 実際の環境では DStructureEntry.openStructure(plugin, filename) で永続化されたデータ構造を得られます。
        DebugSerialization serialization = new DebugSerialization();
        var structure = DStructureEntry.debugStructure(serialization);

        var listEntry = structure.getStructureList("list");
        var entry1 = listEntry.add();

        var entryInt1 = entry1.getLongList("numbers");
        entryInt1.add((long)Integer.MAX_VALUE + 50L);
        entryInt1.add(10L);
        serialization.saveConfig();

        logger.info(()-> serialization.toString());
        DebugSerialization readSerialization = new DebugSerialization(serialization.toString());
        for(var num : DStructureEntry.debugStructure(readSerialization).getStructureList("list").get(0).getLongList("numbers")){
            logger.info(()-> "Num: " + num);
        }
    }
}
