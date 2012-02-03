import org.scalatra._
import java.net.URL
import scala.xml._
import scalate.ScalateSupport
import com.mongodb._
import java.util.Date
import java.text.SimpleDateFormat
import com.mongodb.casbah.Imports._
import utils.Html

class MyScalatraServlet extends ScalatraServlet with ScalateSupport {
  val mongo = MongoConnection()
  val coll = mongo("blog")("msgs")
  val df = new SimpleDateFormat("dd/MM/yyyy HH:mm")

  get("/") {
    contentType = "text/html"
    val content:NodeSeq = {
      <div >
       { for (l <- coll) yield {
         <ul class="list">{
	        l.map((item) => item match {
	          case ("_id", v) => <a>edit</a> % {Attribute (None, "href", Text("edit/" + v), Null)}
	          case (k, v: Date) => <li><b>{ k }</b> - { df.format(v) }</li>
	          case (k, v) => <li><b>{ k }</b> - { v }</li>
	          case _ => <li>???</li>
	        })
         }
        </ul>
       }
      }
      </div>
    }
    templateEngine.layout("/WEB-INF/views/index.scaml", Map("content" -> content))
  }
  
  get("/edit/:id"){
    contentType = "text/html"
    val toEditId = MongoDBObject("_id" -> new ObjectId(params("id")))
    val toEdit = coll.findOne(toEditId)
    val form = toEdit.map(x => 
      <fieldset>{
		      for (key <- x.keys) yield {
		        key match {
		          case "_id" => Html.input("_id", x.getAs[ObjectId]("_id").toString(), "hidden")
		          case _ => Html.input(key, x.getOrElse(key, "").toString())
		        }
		      }
			}
      </fieldset>).getOrElse(<p>"No data found"</p>)
    templateEngine.layout("/WEB-INF/views/form.scaml", Map("form" -> Html.form(action="", children=form)))
  }

  get("/form") {
    contentType = "text/html"
    templateEngine.layout("/WEB-INF/views/form.scaml", Map("name" -> "", "value"->""))
  }

  post("/msgs") {
    val builder = MongoDBObject.newBuilder
    builder += params("key") -> params("value")
    builder += "time" -> new Date()

    coll += builder.result.asDBObject
    redirect("/")
  } 

}
