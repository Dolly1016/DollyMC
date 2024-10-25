package jp.dreamingpig.dollyMC.utils.serialization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTest {

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
}
