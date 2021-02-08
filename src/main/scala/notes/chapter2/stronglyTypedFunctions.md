# Strongly Typed Functions

# Value Classes
- In vanilla Scala, we can wrap a single field and extend the AnyVal abstract class to avoid some runtime costs.

```scala
case class Username(value: String) extends AnyVal
case class Email(value: String) extends AnyVal

def lookup(username: Username, email: Email): F[Option[User]] = ???
```

But we can still be confusing with the params (at least intentionally);
point being the compiler doesn’t help us and that is all we need

A way around this is to make the case class constructors private
and give the user smart constructors.

```scala
case class Username private(val value: String) extends AnyVal
case class Email private(val value: String) extends AnyVal

def mkUsername(value: String): Option[Username] =
  if (value.nonEmpty) Username(value).some else none[Username]
  
def mkEmail(value: String): Option[Email] = if (value.contains("@")) Email(value).some else none[Email]
```

We can STILL get round this using `.copy`. We have to get rid of case classes to truly solve problem.

```scala
sealed abstract class Username(value: String)
sealed abstract class Email(value: String)
```

## New Types

Value classes are fine if used with caution, but we haven’t talked about their limitations and performance issues.
In many cases, Scala needs to allocate extra memory when using value classes.

Lets use the NewType library, which gives us zero-cost wrappers with no runtime overhead. 


```
note: Scala 3 will give us opaque types
```

## Refinement Types

We have seen how `newtypes` help us tremendously in our strongly-typed functions quest.
Nevertheless, it requires smart constructors to validate input data, which adds boilerplate and leaves us with a bittersweet feeling.
Therefore, taking us to our last hope:

refinement types and the Refined5 library.

example: username b=must contain letter `g`
```scala
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.Contains
type Username = String Refined Contains['g']

def lookup(username: Username): F[Option[User]]

lookup("") // error
lookup("aeinstein") // error
lookup("csagan") // compiles
```

In our case non empty string should be fine, so lets combine

```scala
@newtype case class Brand(value: NonEmptyString)
@newtype case class Category(value: NonEmptyString)
val brand: Brand = Brand("foo")
```