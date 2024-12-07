package jp.dreamingpig.dollyMC.economics;

import jp.dreamingpig.dollyMC.utils.serialization.DPrimitiveEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;

public class Currency {
    private DStructureEntry myEntry;
    private DPrimitiveEntry<String> displayNameEntry;
    private DPrimitiveEntry<Integer> decimalPointEntry;
    private DPrimitiveEntry<String> commandIdEntry;

    /**
     * Economicsが通貨を削除する際に使用します。
     * @return
     */
    DStructureEntry getEntry(){
        return myEntry;
    }

    public Currency(DStructureEntry entry, String id){
        this.displayNameEntry = entry.getString("displayName", "名称未設定通貨");
        this.commandIdEntry = entry.getString("commandId", "");
        this.decimalPointEntry = entry.getInt("decimalPoint", 0);
    }

    public int getDecimalPoint(){
        return decimalPointEntry.get();
    }
}
