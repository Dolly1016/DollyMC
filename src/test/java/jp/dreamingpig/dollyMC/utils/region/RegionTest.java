package jp.dreamingpig.dollyMC.utils.region;

import jp.dreamingpig.dollyMC.utils.MathHelper;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureListEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DebugSerialization;
import org.bukkit.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegionTest {
    @Test
    void regionMapWorks() {
        //リージョンマップにコンテンツを追加し、地点からコンテンツを取得できる

        RegionMap<RegionContent> map = new RegionMap<>(DStructureEntry.debugStructure(new DebugSerialization()), RegionContent::new, RegionContent::new);

        var content = map.register(new Area("testWorld", 0, 9,0,9,0,9));
        var got = map.get("testWorld", 5, 5, 5);

        assertSame(content, got, "指定地点の領域コンテンツを取得できます。");
    }
}
