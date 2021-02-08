package shoppingCart.domain

/**
• name: card holder’s name.
• number: a 16-digit number.
• expiration: a 4-digit number as a string, to not lose zeros: the first two digits
  indicate the month, and the last two, the year, e.g. “0821”.
• cvv: a 3-digit number. CVV stands for Card Verification Value.
 */

case class Card(name: String) // TODO


