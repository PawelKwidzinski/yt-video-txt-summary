@param String summary

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Processing Result</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">
<div class="container mx-auto px-4 py-8">
    <div class="max-w-2xl mx-auto bg-white rounded-lg shadow-md p-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-6">Processing Result</h1>

        <div class="mb-6">
            <h2 class="text-xl font-semibold mb-2">Generated Summary:</h2>
            <div class="whitespace-pre-wrap bg-gray-50 p-4 rounded-lg">${summary}</div>
        </div>

        <a href="/" class="inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition duration-200">
            ← Process Another Video
        </a>
    </div>
</div>
</body>
</html>