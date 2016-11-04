package tweets.reactive

/**
  * Created by sankova on 11/3/16.
  */
final case class Author(name: String)

final case class Hashtag(name: String){
  require(name.startsWith("#"), "Hash tag must start with #")
}

final case class Tweet(author: Author, body: String) {
  def hashtags: Set[Hashtag] =
    body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}
