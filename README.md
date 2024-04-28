# Explore_the_galaxy
[![Github All Releases](https://img.shields.io/github/downloads/MilanTodorovic/Explore_the_galaxy/total.svg)]()


<p align="center"><img src="./icon.png" /></p>
<p align="center">A console script for Starsector</p><br><br>


**IMPORTANT NOTICE**: **Old versions** of Better Colonies' AddAdminBC command can cause errors with this mod. It's a known issue and the only way around it would be to edit the AddAdminBC commands code. This post explains how to achieve it: https://fractalsoftworks.com/forum/index.php?topic=17103.msg320152#msg320152


This mod benefits mostly users that have Grand or Adjusted sector mods.<br>
Commands:<br>
1) Explore \[all] \[exclude/include] \[hazard] \[gate] \[stableloc] \[jumppoint] \[planets] \[conditions]<br>
Searches all star systems for specified conditions:<br>
*__all__* - write `all` to search even planets you haven't surveyed yet (default doesn’t search, __if searched, sets survey level to full__);<br>
*__exclude/include__* - write `exclude`/`include`exclude or include systems in the Core world and claimed systems (default: `exclude`)<br>
*__planets__* -  the __minimum__ number of planets a system must include (default: `planets=1`)<br>
*__gate__* - write `gate` to filter by systems that have a gate;<br>
*__stableloc__* - write `stableloc=NUMBER` to filter by stable locations (default `0`);<br>
*__jumppoint__* - same as `stableloc` (default `0`);<br>
*__hazard__* - write `hazard=NUMBER` to filter by systems which planets are below or equal to the specified value (doesn’t filter by default);<br>
*__\[conditions]__* - a comma-separated, __no-space__ list of planetary conditions that must be met (supports * as a wildcard), e.g.: `\[habitable=2,rare_ore*=1,*abundant=1]` **at least** TWO habitable planets and ONE with transplutonic ore and one with ore/transplutonic ore that is abundant.<br>

    Example: Explore all exclude planets=5 \[habitable=2] gate
    
2) SystemInRange \<entity>\[entity] \[LY] \[planets]<br>
Finds systems in specified range (integer) of an entity (coronal_tap, derelict_cryosleeper, inactive_gate):<br>
if two entities are listed `coronal_tap,cryosleeper`, a table will be shown at the end listing systems within range of both;<br>
*__LY__* - distance in  light years (default is 10);<br>
*__planets__* - filters by number of planets<br>

    Example: SystemInRange coronal_tap,cryosleeper 10


<img src="./mod_pictures/mod_explore.jpg">
<img src="./mod_pictures/mod_systeminrange.jpg">


#TODO:
- <strike>filter by stable locations/jump points</strike>
- <strike>filter by inactive gates</strike>
- filter by planet type
