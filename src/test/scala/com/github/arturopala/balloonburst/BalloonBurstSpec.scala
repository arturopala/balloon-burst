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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BalloonBurstSpec extends AnyWordSpec with Matchers {

  import BalloonBurst._

  "BalloonBurst program" should {
    "ask for limits on start" in {
      program(Start, "") shouldBe (InputBalloonList, Ask :: Nil)
    }
    "parse limits input and process to game if success" in {
      program(InputBalloonList, "1") shouldBe (InflatingBalloons(List(1), 0, 0), Ask :: Nil)
      program(InputBalloonList, "1 2 3") shouldBe (InflatingBalloons(List(1, 2, 3), 0, 0), Ask :: Nil)
      program(InputBalloonList, " 1  2    3 ") shouldBe (InflatingBalloons(List(1, 2, 3), 0, 0), Ask :: Nil)
      program(InputBalloonList, "100000") shouldBe (InflatingBalloons(List(100000), 0, 0), Ask :: Nil)
      program(InputBalloonList, "  13 134 9 899998   ") shouldBe (InflatingBalloons(List(13, 134, 9, 899998), 0, 0), Ask :: Nil)
    }
    "parse limits input and ask again if failure" in {
      program(InputBalloonList, "") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, " ") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "a") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "?") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "a1") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "a 1") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "1 a") shouldBe (InputBalloonList, Ask :: Nil)
      program(InputBalloonList, "1 a 3") shouldBe (InputBalloonList, Ask :: Nil)
    }
    "take INFLATE command and inflate balloon" in {
      program(InflatingBalloons(1 :: Nil, 0, 0), "INFLATE") shouldBe (InflatingBalloons(1 :: Nil, 1, 0), Ask :: Nil)
      program(InflatingBalloons(2 :: 2 :: Nil, 1, 7), "INFLATE") shouldBe (InflatingBalloons(2 :: 2 :: Nil, 2, 7), Ask :: Nil)
    }
    "take INFLATE command and burst balloon if limit exceeded, then play next if available" in {
      program(InflatingBalloons(1 :: 1 :: 7 :: Nil, 1, 0), "INFLATE") shouldBe (InflatingBalloons(1 :: 7 :: Nil, 0, 0), Show("BURST") :: Ask :: Nil)
      program(InflatingBalloons(2 :: 2 :: Nil, 2, 7), "INFLATE") shouldBe (InflatingBalloons(2 :: Nil, 0, 7), Show("BURST") :: Ask :: Nil)
    }
    "take INFLATE command and burst balloon if limit exceeded, then show score if finished" in {
      program(InflatingBalloons(1 :: Nil, 1, 0), "INFLATE") shouldBe (Score(0), Show("BURST") :: Show("SCORE: 0") :: Nil)
      program(InflatingBalloons(2 :: Nil, 2, 7), "INFLATE") shouldBe (Score(7), Show("BURST") :: Show("SCORE: 7") :: Nil)
    }
    "take BANK command, score current tries, and play next balloon if available" in {
      program(InflatingBalloons(1 :: 1 :: Nil, 0, 5), "BANK") shouldBe (InflatingBalloons(1 :: Nil, 0, 5), Ask :: Nil)
      program(InflatingBalloons(5 :: 2 :: Nil, 5, 0), "BANK") shouldBe (InflatingBalloons(2 :: Nil, 0, 5), Ask :: Nil)
      program(InflatingBalloons(3 :: 1 :: Nil, 3, 4), "BANK") shouldBe (InflatingBalloons(1 :: Nil, 0, 7), Ask :: Nil)
      program(InflatingBalloons(2 :: 2 :: Nil, 2, 7), "BANK") shouldBe (InflatingBalloons(2 :: Nil, 0, 9), Ask :: Nil)
    }
    "take BANK command, score current tries, then show score if finished" in {
      program(InflatingBalloons(1 :: Nil, 0, 5), "BANK") shouldBe (Score(5), Show("SCORE: 5") :: Nil)
      program(InflatingBalloons(5 :: Nil, 5, 0), "BANK") shouldBe (Score(5), Show("SCORE: 5") :: Nil)
      program(InflatingBalloons(3 :: Nil, 3, 4), "BANK") shouldBe (Score(7), Show("SCORE: 7") :: Nil)
      program(InflatingBalloons(2 :: Nil, 2, 7), "BANK") shouldBe (Score(9), Show("SCORE: 9") :: Nil)
    }
    "take unknown command and ask user again" in {
      program(InflatingBalloons(1 :: Nil, 0, 5), "FOO") shouldBe (InflatingBalloons(1 :: Nil, 0, 5), Ask :: Nil)
      program(InflatingBalloons(2 :: 2 :: Nil, 2, 7), "BAR") shouldBe (InflatingBalloons(2 :: 2 :: Nil, 2, 7), Ask :: Nil)
    }
    "show final score and terminate" in {
      program(Score(0), "") shouldBe (Score(0), List(Show("SCORE: 0")))
      program(Score(7), "") shouldBe (Score(7), List(Show("SCORE: 7")))
      program(Score(7789), "") shouldBe (Score(7789), List(Show("SCORE: 7789")))
    }
    "show score again in the unlikely event of an empty game" in {
      program(InflatingBalloons(Nil, 2, 7), "INFLATE") shouldBe (Score(7), List(Show("SCORE: 7")))
      program(InflatingBalloons(Nil, 2, 7), "BANK") shouldBe (Score(7), List(Show("SCORE: 7")))
    }
  }

}
