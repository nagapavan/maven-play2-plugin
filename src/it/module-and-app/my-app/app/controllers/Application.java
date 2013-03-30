package controllers;

import model.FormData;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render());
    }

    public static Result process() {
        Form<FormData> form = Form.form(FormData.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest("authenticity validation FAILED. This request was forged.");
        } else {
            return ok("authenticity validation PASSED. Now hit the Back button and submit the form again.");
        }
    }

}