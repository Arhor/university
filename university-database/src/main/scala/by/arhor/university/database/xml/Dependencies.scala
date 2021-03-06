package by.arhor.university.database.xml

import java.io.Serializable
import java.util
import java.util.function.Consumer

import javax.xml.bind.annotation.XmlAccessType

@xmlRootElement(name = "dependencies")
@xmlAccessorType(XmlAccessType.FIELD)
case class Dependencies(
  @xmlElement(name = "dependency") var list: util.List[Dependency],
) extends Serializable {

  def this() = this(null)

  def forEach(action: Consumer[Dependency]): Unit = {
    if (list != null) {
      list.forEach(action)
    }
  }
}
