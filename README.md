# polly
"Water my jasminium plant triggered by a Koubachi plant sensor"
This is mainly a testbed for me playing with Clojure.

'
### Requirements
* A Koubachi plant sensor and API access from https://labs.koubachi.com
* A NETIO switchable wall socket (or anything else you care to integrate)
* Something that actually waters your plant
* A CouchDB server

## Installation

### Hardware Setup
Hardware > Software in this case.
I have build my "watering solution" from spare parts of another project, it's basically hardware used to humidify/mist a terrarium. However any other solution should do.

As I am using magnetic valve to get water straight from a the buildings water line there is a tangiable risk that I experience water damage if this valve is left open. 
I am using a NETIO (http://www.netio-products.com/en/overview/) which permits me to have the "open valve -> wait -> close valve"  just on the NETIO without needing an extra request to close the valve.
In this case, if a requests fails all that happens is that one watering cycle is skipped.


                                                                          ++
                                                                          ||
                  water distribution         +--------+     +--------+    ||
         +------+---------------------------+|Pump    |<---+|Magnetic|<---+| Water
         |      |                            |10bar   |     |Valve   |    || Main Line
         |      |                            +--------+     +--------+    ||
         v      v                                  ^            ^         ||
                                                   |p           |p        ||
                                                   |o           |o        ||
       +  PLANT   +                                |w           |w        ||
       |          |                                |e           |e        ||
       |          |                                |r           |r        ||
       |          |                              +-+------------+-+       ||
       |          |                              | Sockets        |       ||
       |+--------+|                              |(NETIO / LUA)   |       ||
       ||Koubachi||                              +----------------+       ||
       ++------+-++                                         |             ||
               |                                            |             ||
               |                         +------------------+------+      ||
               |W                        |   Wandboard             |      ||
               |I                        |-------------------------|      ||
               |F                        |                         |      ||
               |I                        | +---------------------+ |      ||
               | +------------+          | |Controller (Clojure) | |      ||
               +>|Koubachi API|          | |                     | |      ||
                 |            |<--------+| +---------------------+ |      ++
                 |            |          | |History (CouchDB)    | |
                 +------------+          | |                     | |
                                         | +---------------------+ |
                                         +-------------------------+

## Usage

FIXME: explanation


## Options

FIXME: listing of options this app accepts.



### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2014 Ingomar Otter

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
