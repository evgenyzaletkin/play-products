package controllers

import play.api.mvc.{Action, Controller}
import play.api.Logger
import models.Product

/**
 * Created by evgeny on 20.04.14.
 */
object Products extends Controller{
  def list(page: Int) = Action{
    implicit request =>
      Logger.debug("Page " + page)
      val products = Product.findAll
      Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action{
    NotImplemented
  }

  def edit(ean: Long) = Action{
    NotImplemented
  }

  def update(ean: Long) = Action{
    NotImplemented
  }
}
