package com.example.myapplicationcompose.ui.components

class InputValidator private constructor() {
    private var required: Boolean = false
    private var errorRequired: String = ""
    private var customValidations: HashMap<(text: String) -> Boolean, String> = hashMapOf()
    private var onValidationCallback: (valid: Boolean) -> Unit = {}
    var errorMessage: String = ""
        private set

    class Builder {
        private val inputValidator = InputValidator()

        fun build(): InputValidator {
            return inputValidator
        }

        fun setRequired(required: Boolean, error: String = ""): Builder {
            inputValidator.run {
                this.required = required
                errorRequired = error
            }
            return this
        }

        fun addCustomValidation(
            error: String = "",
            customCallback: (text: String) -> Boolean
        ): Builder {
            inputValidator.customValidations[customCallback] = error
            return this
        }

        fun setOnValidationCallback(callback: (valid: Boolean) -> Unit): Builder {
            inputValidator.run {
                onValidationCallback = callback
            }
            return this
        }
    }

    fun validate(text: String): Boolean {
        System.err.println("Text: $text")
        //errorMessage = ""

        if (required) {
            if (text.isEmpty()) {
                errorMessage = errorRequired
                onValidationCallback(false)
                System.err.println(errorRequired)
                return false
            }
        }

        customValidations.forEach { (customValidation, error) ->
            if (!customValidation(text)) {
                onValidationCallback(false)
                System.err.println(error)
                errorMessage = error
                return false
            }
        }

        errorMessage = ""
        onValidationCallback(true)
        return true
    }

    private fun validateCustomValidations(text: String): Boolean {
        customValidations.forEach { (customValidation, error) ->
            if (!customValidation(text)) {
                errorMessage = error
                return false
            }
        }

        return true
    }
}