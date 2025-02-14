@param pl.kwidz.ytvideotxtsummary.YtSubtitle ytSubtitle
@param String summary = null
@param String message = null
@param String error = null


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YT text summary</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-6">
<div class="max-w-4xl mx-auto bg-white rounded-xl shadow-md overflow-hidden">
    <form class="p-6" action="/save" method="post">
        <div class="border-b border-gray-900/20 pb-4">
            <h1 class="text-base font-semibold leading-7 text-gray-900">Youtube video subtitles summary</h1>
            <p class="mt-1 text-sm leading-6 text-gray-500">Fields marked with an asterisk (*) are required.</p>

            @if(message != null)
                <div class="mt-4 p-4 bg-green-100 text-green-700 rounded">
                    ${message}
                </div>
            @endif

            @if(error != null)
                <div class="mt-4 p-4 bg-red-100 text-red-700 rounded">
                    ${error}
                </div>
            @endif

            <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                <div class="sm:col-span-6">
                    <label for="ytId" class="block text-sm font-medium leading-6 text-gray-900">
                        Youtube Id <span class="text-red-500">*</span>
                    </label>
                    <div class="mt-2">
                        <input type="text"
                               name="ytId"
                               id="ytId"
                               value="${ytSubtitle.getYtId()}"
                               class="block w-full rounded-md border-0 px-3 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
                    </div>
                </div>

                <div class="sm:col-span-6">
                    <label for="language" class="block text-sm font-medium leading-6 text-gray-900">
                        Language <span class="text-red-500">*</span>
                    </label>
                    <div class="mt-2">
                        <input type="text"
                               name="language"
                               id="language"
                               value="${ytSubtitle.getLanguage()}"
                               class="block w-full rounded-md border-0 px-3 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
                    </div>
                </div>

                <div class="sm:col-span-12">
                    <label for="prompt" class="block text-sm font-medium leading-6 text-gray-900">
                        Prompt <span class="text-red-500">*</span>
                    </label>
                    <div class="mt-2">
                        <textarea name="prompt" id="prompt" rows="4"
                                  class="block w-full rounded-md border-0 px-3 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        >${ytSubtitle.getPrompt()}</textarea>
                    </div>
                </div>


            </div>
            <div class="mt-6 flex items-center justify-end gap-x-6">
                <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button>
                <button type="submit"
                        class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                    Save
                </button>
            </div>
        </div>


    </form>
    <div class="pt-0 p-6">
        <div class="sm:col-span-12">
            <label for="summary" class="block text-lg font-medium leading-6 text-gray-900 p-1">
                Summary
            </label>
                <div class="sm:col-span-12">
                    <textarea name="summary" id="summary" rows="12"
                                  class="block w-full rounded-md border-0 px-3 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                        >${summary}</textarea>
                </div>
        </div>
    </div>
</div>
</body>
</html>