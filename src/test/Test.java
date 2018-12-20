import java.util.HashSet;
import java.util.Set;

/**
 * 测试亿级数据的查询
 */
public class Test {

    public void hashMapTest(){

        long startTime  = System.currentTimeMillis();

        Set<Integer> hashSet = new HashSet<>();

        for (int i=0;i<10000000;i++){
            hashSet.add(i);
        }

        Asser.asserTrue(hashSet.contains(50));
        Asser.asserTrue(hashSet.contains(60));
        Asser.asserTrue(hashSet.contains(10));

        long endTime = System.currentTimeMillis();

        System.out.println("耗时："+(endTime-startTime));

    }


    public static void main(String[] args) {
        new Test().hashMapTest();
    }

}

class Asser{

    public static void asserTrue(boolean b){
        System.out.println(b);
    }
}

//布隆过滤
