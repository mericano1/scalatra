package utils
import scala.xml.Elem
import scala.xml.UnprefixedAttribute
import scala.xml.Null

object Imports {
	implicit def pimp(elem:Elem) = new {
      def %(attrs:Map[String,String]) = {
        val seq = for( (n,v) <- attrs ) yield new UnprefixedAttribute(n,v,Null)
        (elem /: seq) ( _ % _ )
      }
    }
}