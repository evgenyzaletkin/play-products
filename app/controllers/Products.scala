package controllers

import play.api.mvc.{Flash, Action, Controller}
import play.api.Logger
import models.Product
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages

/**
 * Created by evgeny on 20.04.14.
 */
object Products extends Controller{

  private val flashError = "error"
  private val flashSuccess = "success"

  private val productForm = Form(
    mapping(
      "ean" -> longNumber.verifying("This ean already exists", verifyUnique(_)),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list(page: Int) = Action{
    implicit request =>
      Logger.debug("Page " + page)
      val products = Product.findAll
      Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action{
    implicit request =>
    val product = Product.findByEan(ean)
    product.map(p => Ok(views.html.products.details(p))).getOrElse(NotFound)
  }

  def newProduct = Action{
    implicit request =>
    val form = if (flash.get(flashError).isDefined)
      productForm.bindFromRequest
    else
      productForm
    Ok(views.html.products.editProduct(form))
  }

  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).
          flashing(Flash(form.data) +
          (flashError -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing(flashSuccess -> message)
      }
    )
  }

  def edit(ean: Long) = Action{
    NotImplemented
  }

  def update(ean: Long) = Action{
    NotImplemented
  }

  private def verifyUnique(ean: Long) = Product.findByEan(ean).isEmpty
}
