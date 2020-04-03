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

class BalloonBurstSimulationSpec extends AnyWordSpec with Matchers {

  "BalloonBurst simulation" should {

    "play 0 balloons, burst, and score 0" in {
      GameSimulator.play("0", "INFLATE") shouldBe
        """ -> 0
          | -> INFLATE
          | <- BURST
          | <- SCORE: 0
          |""".stripMargin
    }

    "play 1 balloon, burst and score 0" in {
      GameSimulator.play("1", "INFLATE", "INFLATE") shouldBe
        """ -> 1
          | -> INFLATE
          | -> INFLATE
          | <- BURST
          | <- SCORE: 0
          |""".stripMargin
    }

    "play 1 balloon, bank and score 1" in {
      GameSimulator.play("1", "INFLATE", "BANK") shouldBe
        """ -> 1
          | -> INFLATE
          | -> BANK
          | <- SCORE: 1
          |""".stripMargin
    }

    "play 2 balloons, bank, burst and score 1" in {
      GameSimulator.play("1 1", "INFLATE", "BANK", "INFLATE", "INFLATE") shouldBe
        """ -> 1 1
          | -> INFLATE
          | -> BANK
          | -> INFLATE
          | -> INFLATE
          | <- BURST
          | <- SCORE: 1
          |""".stripMargin
    }

    "play 2 balloons, burst, bank and score 1" in {
      GameSimulator.play("1 1", "INFLATE", "INFLATE", "INFLATE", "BANK") shouldBe
        """ -> 1 1
          | -> INFLATE
          | -> INFLATE
          | <- BURST
          | -> INFLATE
          | -> BANK
          | <- SCORE: 1
          |""".stripMargin
    }

    "play 2 balloons, bank, bank and score 1" in {
      GameSimulator.play("2 2", "INFLATE", "BANK", "INFLATE", "BANK") shouldBe
        """ -> 2 2
          | -> INFLATE
          | -> BANK
          | -> INFLATE
          | -> BANK
          | <- SCORE: 2
          |""".stripMargin
    }

    "play 2 balloons, burst, burst and score 0" in {
      GameSimulator.play("1 1", "INFLATE", "INFLATE", "INFLATE", "INFLATE") shouldBe
        """ -> 1 1
          | -> INFLATE
          | -> INFLATE
          | <- BURST
          | -> INFLATE
          | -> INFLATE
          | <- BURST
          | <- SCORE: 0
          |""".stripMargin
    }

    "play 2 balloons, bank, bank and score 0" in {
      GameSimulator.play("2 2", "INFLATE", "INFLATE", "BANK", "INFLATE", "INFLATE", "BANK") shouldBe
        """ -> 2 2
          | -> INFLATE
          | -> INFLATE
          | -> BANK
          | -> INFLATE
          | -> INFLATE
          | -> BANK
          | <- SCORE: 4
          |""".stripMargin
    }
  }
}
