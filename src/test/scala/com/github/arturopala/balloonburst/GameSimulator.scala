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

object GameSimulator {

  import BalloonBurst._

  /** Play the game simulating user input. */
  def play(input: String*): String = {

    val output = new StringBuilder
    var queue = input.toList

    val testEnvironment: Environment = {
      case Show(msg) =>
        output.append(" <- ").append(msg).append("\n")
        Nil

      case Ask =>
        queue match {
          case Nil => throw new IllegalStateException("Not enough input lines to run simulation!")
          case head :: tail =>
            output.append(" -> ").append(head).append("\n")
            queue = tail
            head :: Nil
        }
    }

    BalloonBurst.runIn(testEnvironment)

    output.toString
  }
}
