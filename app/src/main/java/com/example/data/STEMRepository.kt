package com.example.data

import android.content.Context
import android.content.SharedPreferences

class STEMRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("stem_explorer_prefs", Context.MODE_PRIVATE)

    // User Profile state
    fun loadUserProfile(): UserProfile {
        val name = prefs.getString("user_name", "Young Explorer") ?: "Young Explorer"
        val avatar = prefs.getString("user_avatar", "robot_explorer") ?: "robot_explorer"
        val xp = prefs.getInt("user_xp", 340)
        val stars = prefs.getInt("user_stars", 45)
        val streak = prefs.getInt("user_streak", 5)
        val completed = prefs.getStringSet("completed_activities", setOf("space_mission", "gravity_exp")) ?: emptySet()
        val discoveries = prefs.getStringSet("discovered_ids", setOf("earth", "mars", "microscope", "steam_engine", "solar_cell", "satellite")) ?: emptySet()

        return UserProfile(
            name = name,
            avatarId = avatar,
            xp = xp,
            stars = stars,
            streakDays = streak,
            completedActivities = completed,
            discoveredIds = discoveries
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit()
            .putString("user_name", profile.name)
            .putString("user_avatar", profile.avatarId)
            .putInt("user_xp", profile.xp)
            .putInt("user_stars", profile.stars)
            .putInt("user_streak", profile.streakDays)
            .putStringSet("completed_activities", profile.completedActivities)
            .putStringSet("discovered_ids", profile.discoveredIds)
            .apply()
    }

    fun loadSettings(): AppSettings {
        return AppSettings(
            soundFxEnabled = prefs.getBoolean("sound_fx", true),
            musicEnabled = prefs.getBoolean("music_on", true),
            hapticEnabled = prefs.getBoolean("haptic_on", true),
            notificationsEnabled = prefs.getBoolean("notif_on", true),
            reducedMotion = prefs.getBoolean("reduced_motion", false),
            largeText = prefs.getBoolean("large_text", false),
            language = prefs.getString("lang", "English") ?: "English"
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putBoolean("sound_fx", settings.soundFxEnabled)
            .putBoolean("music_on", settings.musicEnabled)
            .putBoolean("haptic_on", settings.hapticEnabled)
            .putBoolean("notif_on", settings.notificationsEnabled)
            .putBoolean("reduced_motion", settings.reducedMotion)
            .putBoolean("large_text", settings.largeText)
            .putString("lang", settings.language)
            .apply()
    }

    // Default static STEM content
    fun getSTEMGames(): List<STEMGame> = listOf(
        STEMGame(
            id = "robot_builder",
            title = "Robot Builder",
            category = STEMCategory.ROBOTICS,
            description = "Design, assemble, and test your custom high-tech robot helper!",
            emoji = "🤖",
            durationMin = 5,
            xpReward = 40,
            starsReward = 15,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "coding_game",
            title = "Coding For Kids",
            category = STEMCategory.CODING,
            description = "Arrange logic blocks to guide the rover through obstacles!",
            emoji = "💻",
            durationMin = 7,
            xpReward = 50,
            starsReward = 20,
            difficulty = "Medium"
        ),
        STEMGame(
            id = "circuit_puzzle",
            title = "Circuit Puzzle",
            category = STEMCategory.TECHNOLOGY,
            description = "Connect batteries, wires, switches, and power up the lights!",
            emoji = "⚡",
            durationMin = 5,
            xpReward = 35,
            starsReward = 10,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "rocket_launch",
            title = "Rocket Launch",
            category = STEMCategory.SPACE,
            description = "Assemble nose cone, stages, fuel and blast off into orbit!",
            emoji = "🚀",
            durationMin = 6,
            xpReward = 45,
            starsReward = 15,
            difficulty = "Medium"
        ),
        STEMGame(
            id = "bridge_builder",
            title = "Bridge Builder",
            category = STEMCategory.ENGINEERING,
            description = "Construct strong trusses and test them with heavy vehicles!",
            emoji = "🏗️",
            durationMin = 8,
            xpReward = 50,
            starsReward = 20,
            difficulty = "Medium"
        ),
        STEMGame(
            id = "space_mission",
            title = "Space Mission",
            category = STEMCategory.SPACE,
            description = "Fly across the Solar System, visit planets, and sample asteroids!",
            emoji = "🛸",
            durationMin = 6,
            xpReward = 45,
            starsReward = 15,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "machine_maker",
            title = "Machine Maker",
            category = STEMCategory.ENGINEERING,
            description = "Combine gears, pulleys, and levers to build kinetic contraptions!",
            emoji = "⚙️",
            durationMin = 5,
            xpReward = 35,
            starsReward = 10,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "gravity_exp",
            title = "Gravity Experiment",
            category = STEMCategory.PHYSICS,
            description = "Drop feathers and bowling balls on Earth vs Moon vs Vacuum!",
            emoji = "🍎",
            durationMin = 4,
            xpReward = 30,
            starsReward = 10,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "science_lab",
            title = "Science Lab",
            category = STEMCategory.SCIENCE,
            description = "Mix safe chemical solutions, test pH colors, and discover density!",
            emoji = "🧪",
            durationMin = 6,
            xpReward = 40,
            starsReward = 15,
            difficulty = "Medium"
        ),
        STEMGame(
            id = "solar_system",
            title = "Solar System Explorer",
            category = STEMCategory.SPACE,
            description = "Touch and inspect all 8 planets with 3D scale and live fun facts!",
            emoji = "🪐",
            durationMin = 5,
            xpReward = 35,
            starsReward = 10,
            difficulty = "Easy"
        ),
        STEMGame(
            id = "ai_learning",
            title = "AI Learning Lab",
            category = STEMCategory.AI,
            description = "Train neural patterns to recognize shapes, animals, and signals!",
            emoji = "🧠",
            durationMin = 7,
            xpReward = 50,
            starsReward = 20,
            difficulty = "Medium"
        ),
        STEMGame(
            id = "physics_fun",
            title = "Physics For Kids",
            category = STEMCategory.PHYSICS,
            description = "Experiment with ramps, friction, magnets, and energy loops!",
            emoji = "🧲",
            durationMin = 6,
            xpReward = 40,
            starsReward = 15,
            difficulty = "Easy"
        )
    )

    fun getAchievements(): List<Achievement> = listOf(
        Achievement("first_discovery", "First Discovery", "Make your very first STEM discovery!", "🔍", 20, true),
        Achievement("science_explorer", "Science Explorer", "Complete 3 science lab experiments", "🔬", 40, true),
        Achievement("space_explorer", "Space Explorer", "Visit 5 planets in the Solar System", "🚀", 50, true),
        Achievement("coding_starter", "Coding Starter", "Solve your first algorithm puzzle", "💻", 40, false),
        Achievement("robot_builder", "Robot Builder", "Customize and test a functioning robot", "🤖", 45, true),
        Achievement("young_engineer", "Young Engineer", "Construct a bridge holding 10 cars", "🏗️", 50, false),
        Achievement("physics_explorer", "Physics Explorer", "Test gravity on the Earth and Moon", "⚛️", 35, true),
        Achievement("ai_explorer", "AI Explorer", "Train an AI model to 100% accuracy", "🧠", 60, false),
        Achievement("quiz_master", "Quiz Master", "Score 100% on any 10-question STEM quiz", "🏆", 50, false),
        Achievement("bridge_builder", "Master Architect", "Build a bridge with high structural strength", "🌉", 45, false),
        Achievement("rocket_scientist", "Rocket Scientist", "Successfully launch a multistage rocket", "🛰️", 60, false),
        Achievement("inventor", "Inventor Extraordinaire", "Create 3 different complex machines", "💡", 75, false),
        Achievement("innovation_hero", "Innovation Hero", "Reach the top STEM rank with 1,000+ XP", "🌟", 100, false)
    )

    fun getDailyMissions(): List<DailyMission> = listOf(
        DailyMission("dm_1", "Build a Strong Bridge", "Construct a bridge and test it under load", 1, 1, 30, 10, isCompleted = true, isClaimed = false),
        DailyMission("dm_2", "Solve 5 Science Questions", "Take a quiz and answer 5 questions correctly", 5, 3, 25, 8, isCompleted = false, isClaimed = false),
        DailyMission("dm_3", "Explore Three Planets", "Open the Solar System and examine 3 planets", 3, 3, 25, 8, isCompleted = true, isClaimed = true),
        DailyMission("dm_4", "Finish One Coding Puzzle", "Run a complete block sequence to the star", 1, 0, 35, 12, isCompleted = false, isClaimed = false)
    )

    fun getWeeklyChallenge(): WeeklyChallenge = WeeklyChallenge(
        id = "space_week",
        title = "Space Exploration Week",
        description = "Embark on cosmic adventures, test space trivia, and master orbital mechanics!",
        lessonsDone = 2,
        lessonsTarget = 3,
        quizzesDone = 1,
        quizzesTarget = 2,
        missionsDone = 1,
        missionsTarget = 1,
        badgeTitle = "Space Explorer",
        badgeEmoji = "🚀",
        isCompleted = false
    )

    fun getPlanets(): List<Planet> = listOf(
        Planet("Sun", "☀️", 0, "Yellow Dwarf Star", "1,392,700 km", "5,500°C surface", 0,
            listOf("Contains 99.8% of the Solar System's total mass.", "Light takes about 8 minutes and 20 seconds to reach Earth."),
            "Over 1 million Earths could fit inside the Sun!", 0xFFF59E0B),
        Planet("Mercury", "🌑", 1, "Terrestrial Planet", "4,879 km", "-180°C to 430°C", 0,
            listOf("Smallest planet in our Solar System.", "Has virtually no atmosphere to trap heat."),
            "A day on Mercury lasts 59 Earth days, but its year is only 88 days!", 0xFF94A3B8),
        Planet("Venus", "🌕", 2, "Terrestrial Planet", "12,104 km", "465°C constant", 0,
            listOf("Hottest planet due to a runaway greenhouse effect.", "Spins in the opposite direction of most planets."),
            "Venus is often called Earth's twin sister because of similar size!", 0xFFFBBF24),
        Planet("Earth", "🌍", 3, "Terrestrial Planet", "12,742 km", "15°C average", 1,
            listOf("The only known planet in the universe with living organisms.", "71% of Earth's surface is covered with water oceans."),
            "Earth is the only planet not named after a Greek or Roman deity!", 0xFF0284C7),
        Planet("Mars", "🔴", 4, "Terrestrial Planet", "6,779 km", "-60°C average", 2,
            listOf("Known as the Red Planet due to iron oxide (rust) on its surface.", "Home to Olympus Mons, the largest volcano in the Solar System."),
            "A Martian day (Sol) is only 37 minutes longer than an Earth day!", 0xFFEF4444),
        Planet("Jupiter", "🪐", 5, "Gas Giant", "139,820 km", "-110°C cloud tops", 95,
            listOf("The largest planet; more than twice as massive as all others combined.", "Has a famous storm called the Great Red Spot."),
            "Jupiter rotates so quickly that a day lasts only about 10 hours!", 0xFFF97316),
        Planet("Saturn", "🪐", 6, "Gas Giant", "116,460 km", "-140°C cloud tops", 146,
            listOf("Famous for spectacular rings made of ice, rock, and dust.", "Saturn is so light in density that it could float in water!"),
            "Its rings span up to 282,000 km across, but are only about 10 meters thick!", 0xFFEAB308),
        Planet("Uranus", "🌀", 7, "Ice Giant", "50,724 km", "-195°C", 28,
            listOf("Spins on its side at an extreme 98-degree tilt.", "Its cyan color comes from methane in its upper atmosphere."),
            "Uranus was the first planet discovered using an astronomical telescope!", 0xFF06B6D4),
        Planet("Neptune", "🔵", 8, "Ice Giant", "49,244 km", "-200°C", 16,
            listOf("Most distant major planet from the Sun.", "Has the fastest recorded winds in the solar system, exceeding 2,000 km/h!"),
            "It takes Neptune about 165 Earth years to complete one single orbit!", 0xFF3B82F6)
    )

    fun getQuizQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q1",
            question = "Which force pulls dropped objects toward the ground?",
            options = listOf("Magnetism", "Gravity", "Electricity", "Wind"),
            correctIndex = 1,
            explanation = "Gravity is the invisible force that pulls all objects toward the center of the Earth.",
            category = STEMCategory.PHYSICS,
            emoji = "🍎"
        ),
        QuizQuestion(
            id = "q2",
            question = "What is the closest planet to the Sun in our Solar System?",
            options = listOf("Venus", "Mars", "Mercury", "Earth"),
            correctIndex = 2,
            explanation = "Mercury is the closest planet to the Sun and takes only 88 days to complete an orbit.",
            category = STEMCategory.SPACE,
            emoji = "☀️"
        ),
        QuizQuestion(
            id = "q3",
            question = "In computer programming, what is a 'Loop' used for?",
            options = listOf("Playing music", "Repeating actions multiple times", "Shutting down the computer", "Changing screen brightness"),
            correctIndex = 1,
            explanation = "Loops allow programmers to repeat instructions efficiently without writing them over and over.",
            category = STEMCategory.CODING,
            emoji = "🔁"
        ),
        QuizQuestion(
            id = "q4",
            question = "Which shape is known as the strongest geometric shape in bridge building?",
            options = listOf("Square", "Circle", "Triangle", "Oval"),
            correctIndex = 2,
            explanation = "Triangles cannot be deformed without breaking a side, making them the basis for strong trusses.",
            category = STEMCategory.ENGINEERING,
            emoji = "📐"
        ),
        QuizQuestion(
            id = "q5",
            question = "What component stores electrical energy to power portable devices?",
            options = listOf("Battery", "Switch", "Resistor", "Speaker"),
            correctIndex = 0,
            explanation = "Batteries store chemical energy and convert it into electrical current when connected.",
            category = STEMCategory.TECHNOLOGY,
            emoji = "🔋"
        ),
        QuizQuestion(
            id = "q6",
            question = "What device do robotic rovers use to 'see' their surroundings?",
            options = listOf("Cameras & Lidar Sensors", "Solar Panels", "Wheels", "Batteries"),
            correctIndex = 0,
            explanation = "Cameras and Lidar sensors send visual data and distance pulses so robots can navigate safely.",
            category = STEMCategory.ROBOTICS,
            emoji = "🤖"
        ),
        QuizQuestion(
            id = "q7",
            question = "What state of matter is water when it freezes into ice?",
            options = listOf("Gas", "Liquid", "Solid", "Plasma"),
            correctIndex = 2,
            explanation = "When water cools below 0°C (32°F), its molecules lock into a solid crystal lattice called ice.",
            category = STEMCategory.SCIENCE,
            emoji = "🧊"
        ),
        QuizQuestion(
            id = "q8",
            question = "What is Artificial Intelligence (AI) trained to do best?",
            options = listOf("Cook soup instantly", "Recognize patterns in data", "Teleport across rooms", "Turn lead into gold"),
            correctIndex = 1,
            explanation = "AI algorithms analyze large datasets to learn rules and recognize patterns like images and speech.",
            category = STEMCategory.AI,
            emoji = "🧠"
        ),
        QuizQuestion(
            id = "q9",
            question = "Why does a feather fall at the same speed as a bowling ball inside a vacuum?",
            options = listOf("Because they weigh the same", "There is no air resistance", "Magnets pull them", "They are made of plastic"),
            correctIndex = 1,
            explanation = "Without air resistance, gravity accelerates all objects at the exact same rate regardless of mass!",
            category = STEMCategory.PHYSICS,
            emoji = "🪶"
        ),
        QuizQuestion(
            id = "q10",
            question = "How many planets are in our Solar System?",
            options = listOf("7", "8", "9", "12"),
            correctIndex = 1,
            explanation = "There are 8 recognized major planets: Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, and Neptune.",
            category = STEMCategory.SPACE,
            emoji = "🪐"
        )
    )

    fun getDiscoveries(): List<Discovery> = listOf(
        Discovery("earth", "Planet Earth", STEMCategory.SPACE, "Our blue oasis in the cosmos teeming with life.", "Earth's atmosphere protects us from meteoroids and solar radiation.", "🌍", true),
        Discovery("mars", "Mars Rover", STEMCategory.SPACE, "Robotic scientists rolling on Martian red dunes.", "Rovers have discovered ancient lakebeds and river pebbles on Mars.", "🔴", true),
        Discovery("microscope", "Optical Microscope", STEMCategory.SCIENCE, "Tool revealing microscopic cells and atoms.", "Anton van Leeuwenhoek first saw microscopic 'animalcules' in 1674.", "🔬", true),
        Discovery("steam_engine", "Steam Engine", STEMCategory.ENGINEERING, "Engine powering the Industrial Revolution.", "Uses steam pressure expanding inside pistons to turn heavy wheels.", "🚂", true),
        Discovery("solar_cell", "Solar Cell", STEMCategory.TECHNOLOGY, "Photovoltaic device turning sunlight into clean power.", "Discovered via the photoelectric effect by Edmond Becquerel.", "☀️", true),
        Discovery("satellite", "Communications Satellite", STEMCategory.SPACE, "Orbiting spacecraft connecting people globally.", "Travels around Earth at over 27,000 kilometers per hour.", "🛰️", true),
        Discovery("neural_net", "Neural Network", STEMCategory.AI, "Algorithms inspired by interconnected brain neurons.", "Helps computers classify pictures, translate languages, and play chess.", "🧠", false),
        Discovery("electric_motor", "Electric Motor", STEMCategory.PHYSICS, "Converts electrical currents into rotational torque.", "Powers electric cars, fans, robots, and trains.", "⚙️", false),
        Discovery("dna_helix", "DNA Double Helix", STEMCategory.SCIENCE, "The genetic blueprint coding all biological life.", "If unwound, the DNA in one human cell would stretch 2 meters!", "🧬", false),
        Discovery("suspension_bridge", "Suspension Bridge", STEMCategory.ENGINEERING, "Spanning massive waterways with steel cables in tension.", "The main cables transfer the load down into deep anchorages.", "🌉", false),
        Discovery("quantum_chip", "Quantum Processor", STEMCategory.TECHNOLOGY, "Computes using quantum superposition bits (qubits).", "Can calculate complex molecular simulations in seconds.", "💡", false),
        Discovery("rocket_booster", "Reusable Rocket", STEMCategory.SPACE, "Rocket boosters that land vertically back on Earth.", "Reduces the cost of spaceflight by reusing valuable rocket engines.", "🚀", false)
    )

    fun getTechFacts(): List<FactCard> = listOf(
        FactCard("How Computers Think", "Computers process everything using binary 0s and 1s, representing electrical switches being OFF or ON billions of times per second!", "A modern smartphone has millions of times more computing power than NASA used to land Apollo 11 on the Moon.", "💻"),
        FactCard("The Speed of the Internet", "Information travels through fiber-optic cables beneath oceans as pulses of laser light inside pure glass strands thinner than a human hair.", "Undersea fiber cables carry more than 95% of international internet traffic worldwide.", "🌐"),
        FactCard("Artificial Intelligence", "AI models learn by examining millions of examples. When shown photos of cats, the algorithm identifies common features like whiskers, triangular ears, and slit pupils.", "AI doesn't feel emotions or think like humans; it excels at finding statistical mathematical patterns!", "🧠"),
        FactCard("Space Satellites", "Thousands of satellites orbit Earth in the vacuum of space, providing GPS coordinates, weather forecasts, and global communication.", "The International Space Station orbits Earth every 90 minutes, meaning astronauts experience 16 sunrises every day!", "🛰️"),
        FactCard("Autonomous Robots", "Robots use ultrasonic sensors, cameras, and gyroscopes to map rooms in real time so they avoid bumping into walls or stairs.", "The word 'robot' comes from the Czech word 'robota', meaning labor or hard work.", "🤖"),
        FactCard("Microchips & Transistors", "A modern computer chip the size of a fingernail contains over 10 billion tiny electronic switches called transistors.", "A transistor is thousands of times smaller than a red blood cell!", "🔬")
    )
}
