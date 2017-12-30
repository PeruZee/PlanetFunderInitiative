Step by Step, even The Mount Everest is climbable.

We will donate 5% of every lottery to The Ocean Cleanup project. 10% Loser cashback. 50% Winner pool. Everybody wins.

Contributors are welcome.

# Lotto Dice System

1. The dice is rolled as many times as there are participants.
2. The dice has as many sides as there are participants. 
3. The lottery is choice-less and held every 12 hours (720 minutes).

**[Example 1: 10d10]** 

If there are 10 participants, the die will have 10 sides with numbers ranging from 1-10 and each of the participants will also roll the die 10 times individually. Their 10 rolls are totalled and compared against other participants total roll value to give them a standing. There can be ties.

**[Example 2: 100d100]** 

If there are 100 participants, the die will have 100 sides with numbers ranging from 1-100 and each of the participants will also roll the die 100 times individually. Their 100 rolls are totalled and compared against other participants total roll value to give them a standing. There can be ties.

**A 6-Sided Dice: d6**

<img src="img/a%20die.jfif" class="inline"/>


# TO DO

1. Declare participant TXIDs: 1st position, 2nd position etc. (Blueprint done: Shows ID, will also show actual TXID in string value)
2. The cut off method between Winners and Losers [(p = Maximum Roll Total - Minimum Roll Total/2)] where [p] represents arbitrary percentage of losers per lottery period (rounded to Integer where the number is a Double).
3. Integrate with Stellar Platform.

# DONE

1. Create nSided Rollbased Dice
2. Create 50/50 Lotto Architecture
3. Optimize initial Code
4. Store roll values per participant in an array, compare totals versus totals and display standings.



# Java project files @ ./src/*

1. TXIDForEach.java
2. TXIDList.txt
3. TXIDListZero.txt
4. TXIDListOne.txt


#**Project Homepage:** <a href="https://peruzee.github.io/PlanetFunderDiceLotto/">Project Home</a>

#**Wiki Home:** <a href="https://github.com/PeruZee/PlanetFunderDiceLotto/wiki">Project Wiki</a>

#**Facebook:** <a href="https://www.facebook.com/PlanetFunderInitiative/">Project Facebook</a>

# Output Images:

**A 14 Sided Dice roll example & results (14d14):** 

<img src="img/PlanetFunderOP.png" class="inline"/>

**266 Sided Dice roll result by ID (266d266):**

<img src="img/266d266.png" class="inline"/>

**[Participant number 245]: Total of all rolls is 36077, on a 266 sided dice by rolling the dice 266 times (266d266):**

<img src="img/266d266_245.png" class="inline"/>



**Licensed under the Eclipse Public License 1.0**
