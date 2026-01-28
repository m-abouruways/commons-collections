/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.benchmarks;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.SynchronizedBag;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BagBenchmark {

    private Bag<String> hashBag;
    private Bag<String> syncBag;
    private Bag<String> treeBag;

    @Setup(Level.Iteration)
    public void setup() {
        hashBag = new HashBag<>();
        syncBag = SynchronizedBag.synchronizedBag(new HashBag<>());
        treeBag = new TreeBag<>();

        for (int i = 0; i < 1000; i++) {
            String item = "Item" + i;
            hashBag.add(item);
            syncBag.add(item);
            treeBag.add(item);
        }
    }

    // -------- HashBag --------

    @Benchmark
    public void testHashBagAdd() {
        hashBag.add("X");
    }

    @Benchmark
    public int testHashBagGetCount() {
        return hashBag.getCount("Item500");
    }

    @Benchmark
    public void testHashBagRemove() {
        hashBag.remove("Item500");
    }

    // -------- SynchronizedBag --------

    @Benchmark
    public void testSynchronizedBagAdd() {
        syncBag.add("X");
    }

    @Benchmark
    public int testSynchronizedBagGetCount() {
        return syncBag.getCount("Item500");
    }

    @Benchmark
    public void testSynchronizedBagRemove() {
        syncBag.remove("Item500");
    }

    // -------- TreeBag --------

    @Benchmark
    public void testTreeBagAdd() {
        treeBag.add("X");
    }

    @Benchmark
    public int testTreeBagGetCount() {
        return treeBag.getCount("Item500");
    }

    @Benchmark
    public void testTreeBagRemove() {
        treeBag.remove("Item500");
    }

    // -------- JMH Runner --------

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BagBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .result("target/jmh-result.json")
                .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
