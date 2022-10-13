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

import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker.IdentifierTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MemberSelectTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.Name;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.BugPattern.StandardTags.FRAGILE_CODE;

// @Deprecated
// @Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})

@BugPattern(
    name = "DeprecatedUsage",
    summary = "Usage of deprecated API should be avoided.",
    severity = WARNING,
    tags = FRAGILE_CODE)
public class DeprecatedUsage extends BugChecker
    implements IdentifierTreeMatcher, MemberSelectTreeMatcher {
  private static final String PACKAGE = "com.deprecated.";

  private static boolean match(Name name) {
    return name.toString().startsWith(PACKAGE);
  }

  @Override
  public Description matchIdentifier(IdentifierTree tree, VisitorState state) {
    Symbol symbol = ASTHelpers.getSymbol(tree);
    if (symbol.isDeprecated() && match(symbol.getQualifiedName())) {
      return describeMatch(tree);
    }
    return Description.NO_MATCH;
  }

  @Override
  public Description matchMemberSelect(MemberSelectTree tree, VisitorState state) {
    Symbol symbol = ASTHelpers.getResultType(tree).tsym;
    if (symbol.isDeprecated() && match(symbol.getQualifiedName())) {
      return describeMatch(tree);
    }
    return Description.NO_MATCH;
  }
}
