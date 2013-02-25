package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.mvc

case class Task(hash: Int, url: String)

object Task {

val task = {
	get[Int]("hash") ~
	get[String]("url") map {
		case hash~url => Task(hash, url)
	}
}
	def all(): List[Task] = DB.withConnection { implicit c =>
		SQL("select * from HashToUrl").as(task *)
	}

	def getTask(hash: Int): Option[String] = DB.withConnection { implicit c =>
		val url: Option[String] = SQL(
		"""
			select url from HashToUrl where hash = {hash};
		"""
		)
		.on("hash" -> hash)
		.as(scalar[String].singleOpt)
		url
	}
	
	def create(hash: Int, url:String) {
		DB.withConnection { implicit c =>
			SQL("insert into HashToUrl (hash, url) values ({hash}, {url})").on(
				'hash -> hash,
				'url  -> url
			).executeUpdate()
		}
	}
	
	def delete(hash: Int) {
		DB.withConnection { implicit c =>
			SQL("delete from HashToUrl where hash = {hash}").on(
				'hash -> hash
			).executeUpdate()
		}
	}
	
}