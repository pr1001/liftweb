/*
 * Copyright 2009-2010 WorldWide Conferencing, LLC
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

package net.liftweb {
package json {

import _root_.org.specs.Specification
import _root_.org.specs.runner.{Runner, JUnit}

class XmlBugsTest extends Runner(XmlBugs) with JUnit
object XmlBugs extends Specification {
  import JsonAST._
  import Xml._
  import scala.xml.{Group, Text}

  "HarryH's XML parses correctly" in {
    val xml1 = <venue><id>123</id></venue>
    val xml2 = <venue> <id>{"1"}{"23"}</id> </venue>
    Xml.toJson(xml1) must_== Xml.toJson(xml2)
  }

  "HarryH's XML with attributes parses correctly" in {
    val json = toJson(<tips><group type="Nearby"><tip><id>10</id></tip></group></tips>)
    Printer.compact(render(json)) mustEqual """{"tips":{"group":{"type":"Nearby","tip":{"id":"10"}}}}"""
  }
}

}
}
