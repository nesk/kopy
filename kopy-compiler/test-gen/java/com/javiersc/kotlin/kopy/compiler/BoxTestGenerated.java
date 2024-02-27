

package com.javiersc.kotlin.kopy.compiler;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link com.javiersc.kotlin.kopy.compiler.GenerateKotlinCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("test-data/box")
@TestDataPath("$PROJECT_ROOT")
public class BoxTestGenerated extends AbstractBoxTest {
    @Test
    public void testAllFilesPresentInBox() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("nest-copy-set.kt")
    public void testNest_copy_set() throws Exception {
        runTest("test-data/box/nest-copy-set.kt");
    }

    @Test
    @TestMetadata("no-nest-copy-set.kt")
    public void testNo_nest_copy_set() throws Exception {
        runTest("test-data/box/no-nest-copy-set.kt");
    }

    @Test
    @TestMetadata("simple-1.kt")
    public void testSimple_1() throws Exception {
        runTest("test-data/box/simple-1.kt");
    }

    @Test
    @TestMetadata("simple-2.kt")
    public void testSimple_2() throws Exception {
        runTest("test-data/box/simple-2.kt");
    }

    @Nested
    @TestMetadata("test-data/box/edge")
    @TestDataPath("$PROJECT_ROOT")
    public class Edge {
        @Test
        public void testAllFilesPresentInEdge() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box/edge"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("simple-1.kt")
        public void testSimple_1() throws Exception {
            runTest("test-data/box/edge/simple-1.kt");
        }
    }
}
