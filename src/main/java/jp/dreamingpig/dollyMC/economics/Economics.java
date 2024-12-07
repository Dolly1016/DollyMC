package jp.dreamingpig.dollyMC.economics;

import jp.dreamingpig.dollyMC.utils.serialization.DPrimitiveEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import org.bukkit.enchantments.Enchantment;

import java.math.BigInteger;

public class Economics {
    DStructureEntry myEntry;
    DStructureEntry economicsEntry;

    public class Account{
        private DStructureEntry myEntry;
        private Currency currency;
        private DPrimitiveEntry<BigInteger> amountEntry;

        public BigInteger getAmount(){
            return amountEntry.get();
        }

        public void receive(BigInteger amount){
            amountEntry.set(amountEntry.get().add(amount));
        }

        public boolean payIf(BigInteger amount){
            if(canPay(amount)) {
                receive(amount.negate());
                return true;
            }
            return false;
        }

        public boolean canPay(BigInteger amount){
            return amountEntry.get().compareTo(amount) >= 0;
        }

        public String getAmoungAsString(){
            var oneVal = BigInteger.ZERO;
            var val10 = BigInteger.TEN;
            var decimalPoint = currency.getDecimalPoint();

            String rightStr = "";

            //amount[0]が小数点以上、amount[1]が小数点以下
            var amount = amountEntry.get().divideAndRemainder(oneVal);

            if(decimalPoint > 0) {
                for (int i = 0; i < decimalPoint; i++) oneVal = oneVal.multiply(val10);
                rightStr = String.format("%" + decimalPoint + "s", amount[1].toString()).replace(' ', '0');

                //省略可能な最初の0を発見する
                int lastIndex = rightStr.length();
                for(int i = rightStr.length() - 1;i>=0;i--){
                    if(rightStr.charAt(i) != '0'){
                        lastIndex = i + 1;
                        break;
                    }
                }
                rightStr = rightStr.substring(0, lastIndex);
            }

            StringBuilder leftStrBuilder = new StringBuilder();
            var val100 = BigInteger.valueOf(100);
            while(amount[0].compareTo(BigInteger.ZERO) > 0){
                var next = amount[0].divideAndRemainder(val100);
                if(!leftStrBuilder.isEmpty())
                    leftStrBuilder.insert(0, next[1].toString() + ",");
                else
                    leftStrBuilder.insert(0, next[1].toString());
                amount[0] = next[0];
            }
            if(leftStrBuilder.isEmpty()) leftStrBuilder.append(0);
            var leftStr = leftStrBuilder.toString();

            if(!rightStr.isEmpty())
                return leftStr + "." + rightStr;
            else
                return leftStr;
        }
    }
}
