/*
 * Copyright 2020 Artur Opala
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

package com.github.arturopala.balloonburst

// COVERAGE: OFF
object BalloonBurstCommandLine extends App {

  import BalloonBurst._

  val terminal: Environment = {
    case Show(msg) =>
      println(s"<- $msg")
      Nil

    case Ask =>
      scala.io.StdIn.readLine("-> ").toUpperCase :: Nil
  }

  BalloonBurst.runIn(terminal)

}
