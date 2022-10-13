/*
 * Copyright 2022 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns;

import com.google.errorprone.CompilationTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeprecatedUsageTest {
  private CompilationTestHelper compilationHelper;

  @Before
  public void setUp() {
    compilationHelper = CompilationTestHelper.newInstance(DeprecatedUsage.class, getClass());
  }

  @Test
  public void testClass() {
    compilationHelper
        .addSourceLines(
            "Library.java",
            "package com.deprecated;",
            "@Deprecated",
            "public final class Library {}")
        .addSourceLines(
            "Client1.java",
            "// BUG: Diagnostic contains:",
            "import com.deprecated.Library;",
            "public final class Client1 {",
            "  // BUG: Diagnostic contains:",
            "  private Library lib = null;",
            "}")
        .addSourceLines(
            "Client2.java",
            "public final class Client2 {",
            "  // BUG: Diagnostic contains:",
            "  private com.deprecated.Library lib = null;",
            "}")
        .doTest();
  }
}
