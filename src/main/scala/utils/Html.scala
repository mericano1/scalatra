package utils
import scala.xml.NodeSeq
import utils.Imports._

object Html {
  def input(name:String, value:String, iType:String = "text",label:String ="", help:String=""): NodeSeq = {
    <div class="clearfix">
	  <label>{label}</label>
      {<input/> %  Map("name" -> name, "value"->value, "type" -> iType)}
	  <span class="help-inline">{help}</span>
	</div>
  }
  
  def form(action:String, method:String="GET", children:NodeSeq) :NodeSeq = {
    <form>{children}
	</form> % Map("action" -> action, "method" -> method)
  }

}