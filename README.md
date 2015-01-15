DeVoN - Developers Value Notation
===
- A simple, language neutral notation for representing structured values.
- My main motivation for creating this was to overcome the [json](http://www.json.org) limitation of only allowing string names in its name/value pairs for objects.
- I did look at [edn](https://github.com/edn-format/edn), but decided a simpler notation was better suited to my needs.  Be sure to think about what you are doing, examine your options, and choose the option better suited to your needs.

Goals
===
- simple
- language neutral
- human/machine readable/writable
- complete

Overview
===
There are 5 aspects of this notation


unquoted string

    MUST NOT contain whitespace, single quote, or the symbols ()[]{}

quoted string

    starts and ends with a single quote, internal quotes must be doubled up

unit

    ()
    explicitly indicates no value
    sometimes referred to as 'null', 'unit', or 'void'
    useful for messages intended to un-set a value
    whitespace is not allowed between the '(' and ')'

sequence

    []
    the transport layer MUST preserve order, although the consumer MAY decide order is unimportant
    this can be used to model tuples, ordered sequences, or unordered sets

map

    {}
    a set of key value pairs
    the number of elements MUST be even
    duplicate keys SHOULD NOT be present
    different consumers MAY handle duplicate keys differently
    this can be used to model dictionaries (such as java maps), or records (such as java classes)

Specification
===
- Character Classes (Unicode addresses of the characters that can have special meaning)
- Tokens (Extended Backus–Naur Form for the tokenizer)
- Structure (Extended Backus–Naur Form for the parser)

Character Classes
===
single characters

<table>
    <thead>
    <tr><th>name</th><th>character</th><th>unicode</th></tr>
    </thead>
    <tbody>
    <tr><td>tab          </td><td>\t </td><td>000009</td></tr>
    <tr><td>newline      </td><td>\n </td><td>00000A</td></tr>
    <tr><td>return       </td><td>\r </td><td>00000D</td></tr>
    <tr><td>space        </td><td>' '</td><td>000020</td></tr>
    <tr><td>quote        </td><td>\' </td><td>000027</td></tr>
    <tr><td>open-paren   </td><td>(  </td><td>000028</td></tr>
    <tr><td>close-paren  </td><td>)  </td><td>000029</td></tr>
    <tr><td>open-bracket </td><td>[  </td><td>00005B</td></tr>
    <tr><td>close-bracket</td><td>]  </td><td>00005D</td></tr>
    <tr><td>open-brace   </td><td>{  </td><td>00007B</td></tr>
    <tr><td>close-brace  </td><td>}  </td><td>00007D</td></tr>
    </tbody>
</table>

alternatives

    whitespace               = tab | newline | return | space
    structural               = quote | open-paren | close-paren | open-bracket | close-bracket | open-brace | close-brace
    structural-or-whitespace = structural | whitespace

negations (these do not match end of file)

    unquoted-string-character  = not structural-or-whitespace
    not-quote                  = not quote

Tokens
===

    token                   = open-brace | close-brace | open-bracket | close-bracket | null | quoted-string | unquoted-string | whitespace-block
    null                    = open-paren close-paren
    quoted-string           = quote quoted-contents quote
    unquoted-string         = unquoted-string-character { unquoted-string-character }
    whitespace-block        = whitespace { whitespace }
    quoted-contents         = quoted-string-character { quoted-string-character }
    quoted-string-character = not-quote | two-quotes
    two-quotes              = quote quote

Structure
===

Ignore tokens of type "whitespace-block"

    elements = { element }
    element = map | array | string | null
    map = begin-map pairs end-map
    array = begin-array elements end-array
    string = quoted-word | unquoted-word
    pairs = { pair }
    pair = element element

Planned Features for Scala Implementation
===
- Pretty Print
- Store case class into notation
- Load notation into case class
- Java support

Design Decisions
===

Features considered, but rejected in favor of simplicity
===
- 'here' documents
- double quoted strings
- escape characters

Features almost rejected in favor of simplicity, but kept out of necessity
===
- null support

Sample
===
A complex object, followed by an array, string, null, quoted string, and quoted string with an inner quote

{a{b c}\[d \[e f\]\](){()\[f{g h}()\]{}i}j}\[\[k l\]\[\]m\]n()'o p' 'q '' r'

    {
      a {b c}
      [
        d
        [e f]
      ]
      ()
      {
        ()
        [
          f
          {g h}
          ()
        ]
        {} i
      }
      j
    }
    [
      [k l]
      []
      m
    ]
    n
    ()
    'o p'
    'q '' r'

