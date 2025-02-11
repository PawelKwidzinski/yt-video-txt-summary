# YouTube Video Text Summary

## Overview

YouTube Video Text Summary is a Spring Boot application that allows users to generate summaries of YouTube video subtitles using AI-powered text processing. By providing a YouTube video ID, language, and a custom prompt, users can quickly extract and summarize key content from video transcripts.

## Project Requirements

- Java 17+
- Docker
- Spring Boot
- Ollama (Local AI model runner)
- yt-dlp (for subtitle downloading)

## Dependencies

- Spring Boot 3.x
- Spring AI
- Ollama Chat Model
- Lombok
- Jakarta Validation
- Tailwind CSS (for frontend)
- yt-dlp (Docker container)

## Getting Started

### Prerequisites

1. Install Java Development Kit (JDK) 17 or higher
2. Install Docker
3. Install Ollama
4. Pull a compatible AI model (e.g., deepseek-r1:7b)

### Configuration

Modify `application.properties` to set:
- Ollama base URL
- Chat model configuration

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=deepseek-r1:7b
```

## How to Run the Application

### Using Docker (Recommended)

1. Ensure Docker is running
2. Pull the yt-dlp Docker image ->
```docker pull jauderho/yt-dlp```
3. Start the Ollama service
4. Run the Spring Boot application

### Local Development

```bash
./mvnw spring-boot:run
```

## Key Components

### YtSubDownloaderService

Handles the core functionality of:
- Downloading video subtitles
- Converting subtitles to text
- Generating AI-powered summaries

### YTSubConverter

Cleans and preprocesses subtitle files by:
- Removing timestamps
- Stripping HTML formatting
- Consolidating text

### Web Interface

The application provides a simple web form where users can:
- Enter YouTube video ID
- Select subtitle language
- Provide a custom summarization prompt

## Code Examples

### Subtitle Cleaning Process

```java
void cleanTranscript(String fileName) {
    // Remove timestamps and formatting
    String cleanedLine = line.replaceAll("<[^>]+>", "")
        .replaceAll("\\s+", " ")
        .trim();
}
```

### Summary Generation

```java
ChatResponse response = chatModel.call(
    new Prompt(prompt + "\n" + subtitlesContent)
);
```

## Limitations

- Requires local Ollama setup
- Depends on available subtitles
- Summary quality varies with AI model and prompt

## Contributing

Contributions are welcome! Please submit pull requests or open issues on the project repository.

## Future Improvements

- Support for more AI models
- Enhanced subtitle parsing
- Multilingual support
- Improved error handling

## Conclusion

YouTube Video Text Summary simplifies the process of extracting and summarizing video content, making information more accessible and digestible. Experiment with different prompts to get the most insightful summaries!
