DeVoN - Developers Value Notation
===
- A simple, language neutral notation for representing structured values.
- I wanted something as easy to read as [yaml](http://www.yaml.org/), as easy to parse as [json](http://www.json.org/), and a clean syntax like [edn](https://github.com/edn-format/edn)
- What I did not want was
    - The verbosity of [xml](http://www.w3.org/TR/REC-xml/)
    - The limitation where [json](http://www.json.org) only allows strings as keys
    - The complexity of parsing [yaml](http://www.yaml.org/)
    - The over-specification of implementation details of [edn](https://github.com/edn-format/edn), such as distinguishing between list and vector

Entry Points
===
- If you want to use default settings [DefaultDevonMarshaller](blob/master/core/src/main/scala/com/seanshubin/devon/core/devon/DefaultDevonMarshaller.scala)
- If you want full control over configuration [DevonMarshallerImpl](blob/master/core/src/main/scala/com/seanshubin/devon/core/devon/DevonMarshallerImpl.scala)

Goals
===
- minimal (nothing unnecessary, everything that exists, exists for a reason)
- language neutral (no dependence on implementation data types)
- human readable/writable (can pretty print)
- machine readable/writable (easy to parse and format)
- complete (can support any data structure)

Features
===
- no unnecessary punctuation
- the notation only gives special meaning to 11 characters
- whitespace outside of a quoted string is not significant
- language independent (no language specific words like "true", "false", "null")
- maps to common data structures
- support for dictionaries, arrays, null, and strings
- supports streaming (no top level element)

Intentionally not features
===
- no support for primitives other than strings (not the business of a notation to know about data types)
- no escaping (nothing stops you from assigning meaning to your own escape characters in a string)
- no comments (you build comments into your data structure, and have your consumer ignore them)
- no references (instead, treat a value as an id)

Overview
===
There are 5 aspects of this notation


unquoted string

* MUST NOT contain whitespace, single quote, or the symbols ()[]{}
* an empty string MUST be represented as a quoted string, like so: ''

quoted string

* starts and ends with a single quote, internal quotes must be doubled up
* an empty string MUST be represented as a quoted string, like so: ''

unit

* ()
* explicitly indicates no value
* sometimes referred to as 'null', 'unit', or 'void'
* useful for messages intended to un-set a value
* whitespace is not allowed between the '(' and ')'

sequence

* []
* the transport layer MUST preserve order, although the consumer MAY decide order is unimportant
* this can be used to model tuples, ordered sequences, or unordered sets

map

* {}
* a set of key-value pairs
* the number of elements MUST be even
* duplicate keys SHOULD NOT be present
* the transport layer MUST preserve order, although the consumer MAY decide order is unimportant
* the transport layer MUST preserve duplicate keys, although in the case of key-value pairs with duplicate keys, the consumer MAY enact a rule for deciding which key-value pairs to keep and which to discard
* this can be used to model dictionaries (such as java maps), or records (such as java classes)

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

    token                   = open-brace | close-brace | open-bracket | close-bracket | null | string | whitespace-block
    null                    = open-paren close-paren
    string                  = quoted-string | unquoted-string
    quoted-string           = quote quoted-contents quote
    unquoted-string         = unquoted-string-character { unquoted-string-character }
    whitespace-block        = whitespace { whitespace }
    quoted-contents         = { quoted-string-character }
    quoted-string-character = not-quote | two-quotes
    two-quotes              = quote quote

Structure
===

Ignore tokens of type "whitespace-block".

    elements = { element }
    element = map | array | string | null
    map = open-brace pairs close-brace
    array = open-bracket elements close-bracket
    pairs = { pair }
    pair = element element

Sample
===
A complex object, followed by an array, string, null, quoted string, and quoted string with an inner quote

{a{b c}\[d\[e f\]\](){()\[f{g h}()\]{}i}j}\[\[k l\]\[\]m\]n()'o p' 'q '' r'

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

Alternatives
===

Be sure you think about your needs.
DeVoN was designed to be very good at a specific set of goals.
It was not designed to be everything to everyone.
Before choosing a notation, be sure to consider the advantages and disadvantages of your alternatives.

Here are some alternatives worth considering

- [xml](http://www.w3.org/TR/REC-xml/)
- [json](http://www.json.org/)
- [yaml](http://www.yaml.org/)
- [edn](https://github.com/edn-format/edn)
