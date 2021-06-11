var isWebappRatingValid = false;
var isUsabilityRatingValid = false;
var isCustomernameValid = false;
var isCustomerEmailValid = false;
var areSuggestionsValid = false;

addEventListeners();

function addEventListeners()
{
    var webappRating = getWebappRating();
    for (var indexOfWebappRating = 0; indexOfWebappRating < webappRating.length; indexOfWebappRating++)
    {
        var webappRatingButton = webappRating[indexOfWebappRating];webappRatingButton.addEventListener("change", validateWebappRating);
    }
    var usabilityRating = getUsabilityRating();
    for (var indexOfUsabilityRating = 0; indexOfUsabilityRating < usabilityRating.length; indexOfUsabilityRating++)
    {
        var usabilityRatingButton = usabilityRating[indexOfUsabilityRating]; usabilityRatingButton.addEventListener("change", validateUsabilityRating);
    }
    var customername = getCustomername();
    if (customername !== null)
    {
        customername.addEventListener("blur", validateCustomername);
    }
    var customerEmail = getCustomerEmail();
    if (customerEmail !== null)
    {
        customerEmail.addEventListener("blur", validateCustomerEmail);
    }

    var suggestions = getSuggestions();
    if (suggestions !== null)
    {
        suggestions.addEventListener("blur", validateSuggestions);
    }
    var feedbackForm = getFeedbackForm();
    if (feedbackForm !== null)
    {
        feedbackForm.addEventListener("submit", onFeedbackFormSubmit);
    }
}

function onFeedbackFormSubmit(event)
{
    validateAllInputFields();
    updateFeedbackForm();
    var isFormValidResult = isFormValid();
    if (! isFormValidResult)
        event.preventDefault();
}

function updateFeedbackForm()
{
    var isFormValidResult = isFormValid();
    updateSubmitButton(isFormValidResult);
}

function updateSubmitButton(isFormValid)
{
    var submitButton = getSubmitButton();
    if (null === submitButton)
        return;
    if (isFormValid)
        submitButton.disabled = false;
    else
        submitButton.disabled = true;
}

function updateErrorMessageField(isInputValueValid, inputTextFieldElement, errorMessageElementID, errorMessage)
{
    if (isInputValueValid)
        inputTextFieldElement.style.borderColor = "black";
    else
        inputTextFieldElement.style.borderColor = "red";

    updateErrorMessage(isInputValueValid, errorMessageElementID, errorMessage);
}

function updateErrorMessage(isInputValueValid, errorMessageElementID, errorMessage)
{
    if (isInputValueValid)
        hideErrorMessage(errorMessageElementID);
    else
        showErrorMessage(errorMessage, errorMessageElementID);
}

function showErrorMessage(errorMessage, errorMessageElementID)
{
    var errorMessageElement = document.getElementById(errorMessageElementID);
    if (null === errorMessageElement)
        return;
    errorMessageElement.innerText = errorMessage;
    errorMessageElement.style.display = "block";
}

function hideErrorMessage(errorMessageElementID)
{
    var errorMessageElement = document.getElementById(errorMessageElementID);
    if (null === errorMessageElement)
        return;
    errorMessageElement.innerText = "";
    errorMessageElement.style.display = "none";
}

function validateAllInputFields()
{
    validateWebappRating();
    validateUsabilityRating();
    validateCustomername();
    validateCustomerEmail();
    validateSuggestions();

}

function validateWebappRating()
{
    isWebappRatingValid = checkIfWebappRatingIsValid();
    var errorMessageElementID = "webapp-error";
    var errorMessage = "Please rate our Webapp";

    updateErrorMessage(isWebappRatingValid, errorMessageElementID, errorMessage);
    updateFeedbackForm();
}

function checkIfWebappRatingIsValid()
{
    var webappRating = getWebappRating();
    return isAnyButtonChecked(webappRating);
}

function validateUsabilityRating()
{
    isUsabilityRatingValid = checkIfUsabilityRatingIsValid();
    var errorMessageElementID = "usability-error";
    var errorMessage = "Please rate the Usability";

    updateErrorMessage(isUsabilityRatingValid, errorMessageElementID, errorMessage);
    updateFeedbackForm();
}

function checkIfUsabilityRatingIsValid()
{
    var usabilityRating = getUsabilityRating();
    return isAnyButtonChecked(usabilityRating);
}

function validateCustomername()
{
    isCustomernameValid = checkIfCustomernameIsValid();
    var customername = getCustomername();
    var errorMessageElementID = "name-error";
    var errorMessage = "Please enter your name";

    updateErrorMessageField(isCustomernameValid, customername, errorMessageElementID, errorMessage);
    updateFeedbackForm();
}

function checkIfCustomernameIsValid()
{
    var customername = getCustomername();
    if (null === customername)
        return false;

    var customername = customername.value.trim();
    if (customername.length > 0)
        return true;
    else
        return false;
}

function validateCustomerEmail()
{
    isCustomerEmailValid = checkIfCustomerEmailIsValid();
    var customerEmail = getCustomerEmail();
    var errorMessageElementID = "mail-error";
    var errorMessage = "'" + customerEmail.value + "' is not a valid e-mail address";

    updateErrorMessageField(isCustomerEmailValid, customerEmail, errorMessageElementID, errorMessage);
    updateFeedbackForm();
}

function checkIfCustomerEmailIsValid()
{
    var customerEmail = getCustomerEmail();
    if (null === customerEmail)
        return false;

    var customerEmail = customerEmail.value.trim();
    if (0 === customerEmail.length)
        return false;
    var simpleEmailRegex = /^[^@]+@[^@.]+\.[^@]+/;
    var regexMatchesCustomerEmail = simpleEmailRegex.test(customerEmail);
    if (regexMatchesCustomerEmail)
        return true;
    else
        return false;
}

function validateSuggestions()
{
    areSuggestionsValid = checkIfSuggestionsAreValid();
    var suggestions = getSuggestions();
    var errorMessageElementID = "suggestions-error";
    var errorMessage = "Please enter at least 20 characters";

    updateErrorMessageField(areSuggestionsValid, suggestions, errorMessageElementID, errorMessage);
    updateFeedbackForm();
}

function checkIfSuggestionsAreValid()
{
    var suggestions = getSuggestions();
    if (null === suggestions)
        return false;

    var suggestions = suggestions.value.trim();
    if (suggestions.length >= 20)
        return true;
    else
        return false;
}

function getWebappRating()
{
    return document.querySelectorAll("input[name=\"rating-webapp\"]");
}

function getUsabilityRating()
{
    return document.querySelectorAll("input[name=\"rating-usability\"]");
}

function getCustomername()
{
    return document.getElementById("customer-name");
}

function getCustomerEmail()
{
    return document.getElementById("customer-email");
}

function getSuggestions()
{
    return document.getElementById("suggestions");
}

function getSubmitButton()
{
    var submitButtons = document.querySelectorAll("input[type=\"submit\"]");
    if (submitButtons.length >= 1)
        return submitButtons[0];
    else
        return null;
}

function getFeedbackForm()
{
    if (document.forms.length > 0)
        return document.forms[0];
    else
        return null;
}

function isAnyButtonChecked(ButtonsArray)
{
    for (var indexOfButtonsArray = 0; indexOfButtonsArray < ButtonsArray.length; indexOfButtonsArray++)
    {
        var Button = ButtonsArray[indexOfButtonsArray];
        if (Button.checked)
            return true;
    }
    return false;
}

function isFormValid()
{
    if (! isWebappRatingValid)
        return false;
    else if (! isUsabilityRatingValid)
        return false;
    else if (! isCustomernameValid)
        return false;
    else if (! isCustomerEmailValid)
        return false;
    else if (! areSuggestionsValid)
        return false;
    else
        return true;
}
