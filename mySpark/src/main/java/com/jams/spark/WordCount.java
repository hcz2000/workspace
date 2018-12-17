package com.jams.spark;

import java.util.Arrays;
import java.util.Iterator;
<<<<<<< HEAD

=======
>>>>>>> refs/remotes/origin/master
import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;  
import org.apache.spark.api.java.JavaRDD;  
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.FlatMapFunction;  
import org.apache.spark.api.java.function.Function2;  
import org.apache.spark.api.java.function.PairFunction;  
import org.apache.spark.api.java.function.VoidFunction;  
  
import scala.Tuple2;  
  
/** 
 * @author hcz 
 */  
public class WordCount {  
  
    public static void main(String[] args) {  
          
        SparkConf conf = new SparkConf()  
                .setAppName("Spark WordCount by Java.").setMaster("local");  
          
        JavaSparkContext sc = new JavaSparkContext(conf);  
          
<<<<<<< HEAD
        JavaRDD<String> lines = sc.textFile("/etc/password");  

=======
        JavaRDD<String> lines = sc.textFile("D://download//README");  
          
>>>>>>> refs/remotes/origin/master
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {  
<<<<<<< HEAD
            public Iterator<String> call(String line) throws Exception { 
=======
            public Iterator<String> call(String line) throws Exception { //  
>>>>>>> refs/remotes/origin/master
                return Arrays.asList(line.split(" ")).iterator();  
            }  
        });  
          
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {  
<<<<<<< HEAD
=======
            //@Override  
>>>>>>> refs/remotes/origin/master
            public Tuple2<String, Integer> call(String word) throws Exception {  
                return new Tuple2<String,Integer>(word,1) ;  
            }  
        });  
          
        JavaPairRDD<String,Integer> wordsCount =   
                pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {   
<<<<<<< HEAD
             public Integer call(Integer v1, Integer v2) throws Exception {  
=======
            //@Override  
            public Integer call(Integer v1, Integer v2) throws Exception {  
>>>>>>> refs/remotes/origin/master
                return v1 + v2;  
            }  
        });  
          
        wordsCount.foreach(new VoidFunction<Tuple2<String,Integer>>() {  
<<<<<<< HEAD
=======
            //@Override  
>>>>>>> refs/remotes/origin/master
            public void call(Tuple2<String, Integer> pairs) throws Exception {  
                System.out.println(pairs._1 + ":" + pairs._2);  
            }  
        });   
<<<<<<< HEAD
       sc.close();  
=======
        sc.close();  
>>>>>>> refs/remotes/origin/master
    }  
}  
 