# Console Galaxy
[![Github All Releases](https://img.shields.io/github/downloads/Anexgohan/ConsoleGalaxy/total.svg)]()


[![Download Link click Image Above](https://github.com/Anexgohan/ConsoleGalaxy/blob/main/icon.png)](https://github.com/Anexgohan/ConsoleGalaxy/releases/latest/)
### Click Image above to download the latest version


## All releases are tested and working in Starsector v0.97a.RC11

<details>
  <summary>This mod is a fork:</summary>
    
  ```
  [0.95a] Explore the Galaxy 1.2.3
  ```

  [Explore the Galaxy - Starsector Forum Link](https://fractalsoftworks.com/forum/index.php?topic=21800.0)
  
</details>


### Search for your ideal system with the Console Commands, easy to use:

## Commands:

+ Explore [all] [exclude/include] [hazard] [gate] [stableloc] [jumppoint] [planets] [conditions]
    Searches all star systems for specified conditions:
    - all - write `all` to search even planets you haven't surveyed yet (default doesn’t search, if searched, sets survey level to full);
    - exclude/include - write `exclude/include` to exclude or include systems in the Core world and claimed systems (default: exclude)
    - planets - the minimum number of planets a system must include (default: planets=1)
    - gate - write `gate` to filter by systems that have a gate;
    - stableloc - write `stableloc=NUMBER` to filter by stable locations (default 0);
    - jumppoint - same as stableloc (default 0);
    - hazard - write `hazard=NUMBER` to filter by systems which planets are below or equal to the specified value (doesn’t filter by default);
    - [conditions] - a comma-separated, no-space list of planetary conditions that must be met (supports * as a wildcard), e.g.: \[habitable=2,rare_ore*=1,*abundant=1] at least TWO habitable planets and ONE with transplutonic ore and ONE with ore/transplutonic ore that is abundant.

Example: Explore all exclude planets=5 [habitable=2] gate

+ SystemInRange <entity>[entity] [LY] [planets]
    Finds systems in specified range (integer) of an entity (coronal_tap, derelict_cryosleeper, inactive_gate):
    - if two entities are listed coronal_tap,cryosleeper, a table will be shown at the end listing systems within range of both;
    - LY - distance in light years (default is 10);
    - planets - filters by number of planets

Example: SystemInRange coronal_tap,cryosleeper 10
