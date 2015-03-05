DeVoN - Developers Value Notation
===
- A minimal, complete, language neutral notation for representing structured values.
- I wanted something as easy to read as [yaml](http://www.yaml.org/), as easy to parse as [json](http://www.json.org/), and a clean syntax like [edn](https://github.com/edn-format/edn)
- I choose not to use
    - [xml](http://www.w3.org/TR/REC-xml/), too verbose
    - [json](http://www.json.org), only allows strings as keys, too verbose
    - [yaml](http://www.yaml.org/), too complex to parse
    - [edn](https://github.com/edn-format/edn), more complicated due to its support for advanced types
    - [hocon](https://github.com/typesafehub/config/blob/master/HOCON.md), more complicated due to its support for merging configurations

Goals
===
- minimal (nothing unnecessary, everything that exists, exists for a reason)
- language neutral (no dependence on implementation data types)
- human readable/writable (can pretty print)
- machine readable/writable (easy to parse and format)
- complete (can support any data structure)

Examples
===

Simple strings.
Devon does not require a top level element, which makes it easy to append more devon elements to a file.
An empty string must be represented as two single quotes.
Spaces are only supported within a quoted string.
To represent a single quote in a quoted string, you have to double them up.

    Hello
    World
    ''
    'Hello, world!'
    'Sean''s favorite notation'

An array of urls.
Notice that quotes are not needed since none of the characters used here have special meaning in devon.

    [
      http://example.com/document.txt#line=10,20
      http://example.com/foo.mp4#t=10,20
      http://example.com/bar.webm#t=40,80&xywh=160,120,320,240
    ]

An array of windows paths.
Since devon has no escape characters, nothing special is needed to represent a backslash.
Since devon uses whitespace to support formatting, quoting is needed to represent a space.

    [
      'C:\Program Files'
      C:\Winnt
      C:\Winnt\System32
    ]

A map.  The key is the group and artifact id.
The value is an array of versions.
Unlike other formats, keys are not restricted to strings only.

    {
      {
        group org.joda
        artifact joda-convert
      }
      [
        1.7
        1.6
        1.5
      ]
      {
        group joda-time
        artifact joda-time
      }
      [
        2.7
        2.6
        2.5
      ]
    }

A sample of what an HTTP PATCH request might look like.
We are updating the price of the item with sku 123 to 499.99, and we are unsetting the seasonal discount by sending a null '()'

    {
      sku 123
      price 499.99
      'seasonal discount' ()
    }

Features
===
- no unnecessary punctuation
- the notation only gives special meaning to 11 characters (6 structural, 4 whitespace, and quote)
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
- no references (you can designate a value as an id, and refer to it from another value)

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
* this can be used to model tuples, ordered sequences, or unordered sets
* the transport layer MUST preserve order, although the consumer MAY decide order is unimportant

map

* {}
* a set of key-value pairs
* this can be used to model dictionaries (such as java maps), or records (such as java classes)
* the number of elements MUST be even
* duplicate keys SHOULD NOT be present
* the transport layer MUST preserve order, although the consumer MAY decide order is unimportant
* the transport layer MUST preserve duplicate keys, although in the case of key-value pairs with duplicate keys, the consumer MAY enact a rule for deciding which key-value pairs to keep and which to discard
* the consumer SHOULD ignore keys it does not understand, for forward compatibility

Entry Points
===
- If you want to use default settings [DefaultDevonMarshaller](core/src/main/scala/com/seanshubin/devon/core/devon/DefaultDevonMarshaller.scala)
- If you want full control over configuration [DevonMarshallerImpl](core/src/main/scala/com/seanshubin/devon/core/devon/DevonMarshallerImpl.scala)

Maven
===

    <repository>
        <id>sean</id>
        <name>sean</name>
        <url>http://thoughtfulcraftsmanship.com/nexus/content/repositories/sean-snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>

    <dependency>
      <groupId>com.seanshubin</groupId>
      <artifactId>devon-core</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

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

Implementations
===
Implementations should, at a minimum, provide the following functionality, expressed here as the corresponding method signatures for this Scala reference implementation.

    def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]
    def toCompact(devon: Devon): String
    def toPretty(devon: Devon): Seq[String]
    def fromValue[T](value: T): Devon
    def toValue[T](devon: Devon, theClass: Class[T]): T

The type 'Devon' corresponds to the abstract syntax tree.

    sealed trait Devon
    case object DevonNull extends Devon
    case class DevonString(string: String) extends Devon
    case class DevonArray(array: Seq[Devon]) extends Devon
    case class DevonMap(map: Map[Devon, Devon]) extends Devon

The generic type 'T' corresponds to a Scala case class, primitive, map or sequence

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
- [hocon](https://github.com/typesafehub/config/blob/master/HOCON.md)
