package com.example.data

import com.example.R
import com.example.model.ArticleEntity

object SampleArticles {
    fun getInitialArticles(): List<ArticleEntity> {
        return listOf(
            ArticleEntity(
                id = 1,
                title = "The Architecture of Calm: Why We Need Quiet Interfaces",
                url = "https://theatlantic.com/technology/archive/2026/04/calm-computing-minimalism/704122/",
                domain = "theatlantic.com",
                author = "Elena Vance",
                publishedDate = "Aug 24, 2026",
                timeToReadMinutes = 6,
                excerpt = "In an age of hyperactive notifications and glowing banners, quiet computing is not an aesthetic choice—it is a cognitive necessity.",
                content = """
                    In the late 1990s, computer scientists Mark Weiser and John Seely Brown introduced the concept of "Calm Technology." They envisioned a world where computers would seamlessly fade into the background, providing information without demanding our undivided attention.

                    Thirty years later, our digital reality looks starkly different. Today's software is engineered to trigger dopamine loops, using aggressive badge counts, flashing banners, and endless feeds to monopolize our focus.

                    Quiet computing represents a counter-movement. It proposes that the best software is tool-like, respectful, and silent until called upon. When we interact with an interface designed with restraint, our cognitive load decreases. We regain the ability to engage in deep, uninterrupted thought.

                    Designing for calm requires three fundamental shifts:
                    First, asynchronous by default. Not every ping needs immediate triage. Giving users control over temporal boundaries restores agency.

                    Second, typographic clarity. When information is structured with generous margins, legible type, and intentional hierarchy, the mind absorbs ideas without friction.

                    Third, purposeful conclusion. Infinite feeds create cognitive anxiety because there is no natural finish line. Calm tools provide closure—a sense of 'you are all caught up.'

                    As we continue weaving digital tools deeper into our daily lives, demanding serenity from our screens is no longer just good design; it is self-preservation.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_culture_1788010307706,
                isFavorite = true,
                isArchived = false,
                readingProgress = 0.45f,
                savedAt = System.currentTimeMillis() - 3600000 * 2,
                category = "Culture",
                tags = "Design, DeepWork, Mindset"
            ),
            ArticleEntity(
                id = 2,
                title = "Mapping the Quantum Frontier: What Lies Beyond Classical Computing",
                url = "https://wired.com/story/quantum-computing-frontier-supremacy-2026/",
                domain = "wired.com",
                author = "Dr. Aris Thorne",
                publishedDate = "Aug 22, 2026",
                timeToReadMinutes = 8,
                excerpt = "Recent breakthroughs in topological qubits and error correction are turning theoretical quantum physics into tangible computational power.",
                content = """
                    For decades, quantum computing felt perpetually ten years away. The delicate nature of qubits—prone to decoherence at the slightest thermal perturbation—made scaling these systems an engineering nightmare.

                    However, recent milestones in logical error correction have drastically rewritten the timeline. By entangling multiple physical qubits into single fault-tolerant logical units, researchers have demonstrated sustained coherence times that were unthinkable five years ago.

                    What does this mean for real-world applications?
                    The most immediate impact will be felt in computational chemistry and materials science. Simulating molecular interactions at the quantum level allows us to design novel room-temperature superconductors, discover enzyme-targeted pharmaceuticals, and optimize carbon-capture catalysts.

                    Classical supercomputers, for all their petascale power, struggle with exponential state spaces. A quantum processor operates in Hilbert space, evaluating vast combinatoric spaces simultaneously through quantum superposition and entanglement.

                    As we transition from the NISQ (Noisy Intermediate-Scale Quantum) era to fully fault-tolerant architectures, the boundary between theoretical physics and engineering is dissolving before our eyes.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_tech_1788010278716,
                isFavorite = false,
                isArchived = false,
                readingProgress = 0.1f,
                savedAt = System.currentTimeMillis() - 3600000 * 18,
                category = "Technology",
                tags = "Quantum, AI, Physics"
            ),
            ArticleEntity(
                id = 3,
                title = "Whispers from the Cosmic Dawn: Uncovering the First Galaxies",
                url = "https://nature.com/articles/d41586-026-cosmic-dawn-jwst-galaxies",
                domain = "nature.com",
                author = "Prof. Clara Johansson",
                publishedDate = "Aug 19, 2026",
                timeToReadMinutes = 7,
                excerpt = "Deep infrared observations are revealing massive, luminous stellar nurseries that formed just a few hundred million years after the Big Bang.",
                content = """
                    When astronomers pointed the deepest infrared mirrors toward what appeared to be empty patches of dark sky, they did not expect to find fully formed cosmic monsters.

                    Standard cosmological models predicted that early galaxies would be modest, disorganized clumps of gas and primordial stars. Yet, deep-field spectroscopic data has confirmed the presence of ultra-luminous galaxies dating back to just 300 million years after the universe's birth.

                    How did such massive cosmic structures condense so rapidly?
                    One leading hypothesis points to the role of primordial black holes acting as gravitational seeds. Another suggests that early star formation was exponentially more efficient than previously modeled, aided by the absence of heavy elements.

                    Every photon captured from these distant realms has journeyed through expanding space-time for over 13 billion years, carrying clues about the chemical origins of every atom in our bodies.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_science_1788010293547,
                isFavorite = true,
                isArchived = false,
                readingProgress = 0.8f,
                savedAt = System.currentTimeMillis() - 3600000 * 36,
                category = "Science",
                tags = "Astronomy, Space, Science"
            ),
            ArticleEntity(
                id = 4,
                title = "The Nordic Secret to Long-Term Focus: Friluftsliv and Deep Rest",
                url = "https://bbc.com/worklife/article/2026-nordic-rest-deep-focus-outdoors",
                domain = "bbc.com",
                author = "Soren Lindqvist",
                publishedDate = "Aug 15, 2026",
                timeToReadMinutes = 5,
                excerpt = "How the Scandinavian concept of open-air living and deliberate disconnection repairs attentional fatigue and boosts creative clarity.",
                content = """
                    In Norway and Sweden, there is a cultural philosophy known as 'friluftsliv'—literally translated as 'free-air life.' It is not merely a weekend outdoor hobby; it is a fundamental pillar of psychological restoration.

                    Attention Restoration Theory (ART), pioneered by environmental psychologists Stephen and Rachel Kaplan, explains why natural environments have such a profound effect on our mental state.

                    Urban and digital environments demand 'directed attention'—a metabolically expensive cognitive process that requires filtering out distractions and maintaining conscious focus. Over time, directed attention depletes, leading to mental fatigue, irritability, and diminished problem-solving ability.

                    Nature, by contrast, engages 'soft fascination.' The rustling of pine needles in the wind, the gentle ripple of water across a mountain lake, the shifting patterns of dappled sunlight—these stimuli capture our attention effortlessly, allowing our prefrontal cortex to recover.

                    By pairing intense bursts of intellectual work with periods of true sensory stillness outdoors, we not only protect our mental health, but discover our most creative solutions.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_nature_1788010319815,
                isFavorite = false,
                isArchived = false,
                readingProgress = 0.0f,
                savedAt = System.currentTimeMillis() - 3600000 * 50,
                category = "Productivity",
                tags = "Wellbeing, Focus, Nature"
            ),
            ArticleEntity(
                id = 5,
                title = "The Lost Art of Deep Reading in a Skimming World",
                url = "https://newyorker.com/books/page-turner/the-lost-art-of-deep-reading",
                domain = "newyorker.com",
                author = "Julian Sterling",
                publishedDate = "Aug 10, 2026",
                timeToReadMinutes = 9,
                excerpt = "When we read online, we scan, dart, and scroll. How intentional reading environments can train our brains back into long-form literacy.",
                content = """
                    Cognitive neuroscientist Maryanne Wolf has extensively documented how the medium through which we read shapes the neural circuits of our brain.

                    When reading on screens with hyperlinks, infinite scrolls, and visual interrupts, our eyes adopt an F-shaped scanning pattern. We read the first few words of a paragraph, skim the middle, and jump ahead. We become information foragers rather than contemplative readers.

                    Deep reading, however, is an active construction of meaning. It involves inferential thinking, analogical reasoning, critical analysis, and empathy. When we immerse ourselves in a long essay or book without interruptions, we engage the brain's default mode network—the region responsible for self-reflection and moral understanding.

                    Pocket and reader modes serve as cognitive shields. By stripping away ads, sidebars, tracking widgets, and comment sections, they restore the sacred silence between reader and text.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_culture_1788010307706,
                isFavorite = true,
                isArchived = true,
                readingProgress = 1.0f,
                savedAt = System.currentTimeMillis() - 3600000 * 120,
                category = "Culture",
                tags = "Reading, Books, Philosophy"
            )
        )
    }

    fun getDiscoverArticles(): List<ArticleEntity> {
        return listOf(
            ArticleEntity(
                id = 101,
                title = "The Evolution of Type: How Digital Fonts Reshaped Human Expression",
                url = "https://designobserver.com/feature/evolution-of-digital-typography/40192",
                domain = "designobserver.com",
                author = "Marcus Chen",
                publishedDate = "Today • Editor's Pick",
                timeToReadMinutes = 6,
                excerpt = "From Gutenberg's movable type to variable OpenType fonts on high-DPI displays, typography continues to mirror technological revolutions.",
                content = """
                    Typography is the invisible voice of written language. Every typeface conveys an emotional cadence before a single sentence is consciously parsed.

                    In the transition from physical letterpress to digital screens, typography faced immense challenges. Early bitmap fonts were constrained by low-resolution CRT grids. Today, variable font formats allow fluid optical sizing, weight interpolation, and dynamic responsive adjustment across thousands of screen configurations.

                    Good typography does not call attention to itself; it creates a frictionless bridge between the author's mind and the reader's imagination.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_tech_1788010278716,
                isFavorite = false,
                isArchived = false,
                category = "Design",
                tags = "Design, Typography, Art"
            ),
            ArticleEntity(
                id = 102,
                title = "Bio-Acoustics in the Deep Ocean: Decoding Whale Dialects",
                url = "https://nationalgeographic.com/animals/article/whale-songs-dialects-ai-acoustics",
                domain = "nationalgeographic.com",
                author = "Dr. Sarah Al-Mansoor",
                publishedDate = "Yesterday • Trending",
                timeToReadMinutes = 8,
                excerpt = "Marine biologists equipped with underwater hydrophones and neural networks are deciphering the complex syntax of sperm whale codas.",
                content = """
                    Deep beneath the sunlit oceanic surface, sound travels four times faster than in air. For cetaceans, the ocean is an acoustic amphitheater spanning thousands of nautical miles.

                    Using underwater acoustic arrays and machine learning algorithms, the Project CETI research initiative has cataloged millions of sperm whale vocalizations known as 'codas.'

                    The data reveals astonishing sophistication: phonetic modulation, rhythmic rubato, and distinct regional dialects passed down across generations through matrilineal cultural transmission.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_nature_1788010319815,
                isFavorite = false,
                isArchived = false,
                category = "Science",
                tags = "Nature, Science, Biology"
            ),
            ArticleEntity(
                id = 103,
                title = "The Renaissance of Handcrafted Hardware and Mechanical Objects",
                url = "https://theverge.com/features/handcrafted-tactile-hardware-renaissance",
                domain = "theverge.com",
                author = "Leon Rossi",
                publishedDate = "2 days ago • Must Read",
                timeToReadMinutes = 5,
                excerpt = "Why enthusiasts are abandoning sleek glass slabs in favor of mechanical keyboards, analog dials, and repairable electronics.",
                content = """
                    Touchscreens conquered the world because of their extreme flexibility. A flat sheet of glass can morph into a calculator, a game controller, or a canvas.

                    Yet in trading physical switches for capacitive glass, we lost tactile feedback. There is no muscle memory on a glass pane; your fingers cannot locate a virtual button without visual confirmation.

                    The recent resurgence of custom mechanical keyboards, high-fidelity rotary dials, and machined aluminum cases is a direct response to this sensory impoverishment. We yearn for objects with heft, friction, and satisfying acoustic feedback.
                """.trimIndent(),
                thumbnailResId = R.drawable.article_culture_1788010307706,
                isFavorite = false,
                isArchived = false,
                category = "Technology",
                tags = "Tech, Hardware, Craft"
            ),
            ArticleEntity(
                id = 104,
                title = "Exoplanetary Atmospheres and the Search for Biosignatures",
                url = "https://scientificamerican.com/article/exoplanet-biosignatures-james-webb/",
                domain = "scientificamerican.com",
                author = "Dr. Maya Patel",
                publishedDate = "3 days ago • Deep Dive",
                timeToReadMinutes = 11,
                excerpt = "Transmission spectroscopy is probing the chemical balance of distant terrestrial worlds orbiting red dwarf stars.",
                content = """
                    When an exoplanet passes in front of its host star, a tiny fraction of starlight filters through its atmosphere. Different chemical molecules absorb specific wavelengths, leaving distinct spectral fingerprints.

                    Scientists are now analyzing transmission spectra from the TRAPPIST-1 planetary system. They are searching for chemical disequilibrium—combinations of gases like methane and oxygen that could only co-exist through active biological processes.

                    We are standing on the precipice of answering humanity's oldest question: Are we alone in this vast cosmic tapestry?
                """.trimIndent(),
                thumbnailResId = R.drawable.article_science_1788010293547,
                isFavorite = false,
                isArchived = false,
                category = "Science",
                tags = "Space, Science, Physics"
            )
        )
    }
}
