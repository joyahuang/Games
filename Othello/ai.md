## Smart Move:

1. find unoccupied squares with `player.empty_squares()`, save the position lists as `positions`
2. iterate every `pos` within `positions`
3. calculate the `flip_pos_all`(the to-be-flipped positions of black tiles) for each `pos` assuming if we drop tile in this position
4. find the `max_wnum_increment`, i.e. maximize the white tile increment
5. save `max_flip_pos`, `max_pos`, drop and flip tiles accordingly

## Turn Out:

In most of the times, the computer wins. (Me and my friends are new bees to this game)
Sometimes, I play this game blindfold and still wins.

## Ideas for future improvement:

Now the computer only calculate one step ahead.

In my next improvement, the computer calculate `bnum_increment` each `pos` and calculate all possible next `bnum_increment` for the human player based on this `pos`, minimize the human's black tile increment in human's turn.

If possible, calclulate two steps or more ahead, in this way, the behavior can have long-term strategic and plannning spirit. For example, this mode may encourage the computer take up corner/edge squares, which is suggested by the tutorials of Othello!
