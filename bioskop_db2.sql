-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2025 at 04:01 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bioskop_db2`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `schedule_id` int(11) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `payment_status` varchar(20) NOT NULL DEFAULT 'pending',
  `booking_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `customer_id`, `schedule_id`, `payment_method`, `total_price`, `payment_status`, `booking_date`) VALUES
(1, 1, 5, 'Cash', 100.00, 'pending', '2025-05-17 17:28:39'),
(2, 1, 5, 'Cash', 100.00, 'pending', '2025-05-17 17:46:56'),
(3, 1, 5, 'Cash', 100.00, 'pending', '2025-05-17 17:48:15'),
(4, 1, 6, 'Cash', 100.00, 'pending', '2025-05-17 17:49:09'),
(5, 1, 6, 'Cash', 100.00, 'pending', '2025-05-18 04:38:59');

-- --------------------------------------------------------

--
-- Table structure for table `booking_seat`
--

CREATE TABLE `booking_seat` (
  `booking_id` int(11) NOT NULL,
  `screening_seat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking_seat`
--

INSERT INTO `booking_seat` (`booking_id`, `screening_seat_id`) VALUES
(1, 67),
(1, 68),
(2, 52),
(2, 53),
(3, 54),
(3, 69),
(4, 248),
(4, 249),
(5, 246),
(5, 247);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(10) DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `username`, `email`, `phone_number`, `password`, `role`) VALUES
(1, 'Nakkcad', 'Nakkcad@gmail.com', '083824820226', 'e853301ccf85309403796a2995c429e8534c9ef896e02aae4d26dbe60ec9e4f0', 'admin'),
(2, 'admin', '', NULL, '5a03181f53eebb52998536173eb5bf51db7f816c36fc498092bbed7dd0d60111', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `film`
--

CREATE TABLE `film` (
  `film_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `synopsis` text DEFAULT NULL,
  `poster_url` varchar(255) DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `rating` decimal(3,1) DEFAULT 0.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `film`
--

INSERT INTO `film` (`film_id`, `title`, `genre`, `duration`, `synopsis`, `poster_url`, `release_date`, `rating`) VALUES
(1, 'Havoc', 'Action', 107, 'When a drug heist swerves lethally out of control, a jaded cop fights his way through a corrupt city\'s criminal underworld to save a politician\'s son.', 'https://image.tmdb.org/t/p/w500/r46leE6PSzLR3pnVzaxx5Q30yUF.jpg', '2025-04-24', 6.7),
(2, 'A Working Man', 'Action', 116, 'Levon Cade left behind a decorated military career in the black ops to live a simple life working construction. But when his boss\'s daughter, who is like family to him, is taken by human traffickers, his search to bring her home uncovers a world of corruption far greater than he ever could have imagined.', 'https://image.tmdb.org/t/p/w500/xUkUZ8eOnrOnnJAfusZUqKYZiDu.jpg', '2025-03-26', 6.4),
(3, 'A Minecraft Movie', 'Family', 101, 'Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they\'ll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.', 'https://image.tmdb.org/t/p/w500/iPPTGh2OXuIv6d7cwuoPkw8govp.jpg', '2025-03-31', 6.2),
(4, 'Van Gogh by Vincent', 'Documentary', 46, 'In a career that lasted only ten years, Vincent Van Gogh painted one subject more than any other: himself. This is the story of Vincent told using eight of his most iconic self-portraits.', 'https://image.tmdb.org/t/p/w500/z73X4WKZghBh5fri31o8P6vBEB2.jpg', '2025-03-26', 6.5),
(5, 'Bullet Train Explosion', 'Action', 137, 'When panic erupts on a Tokyo-bound bullet train that will explode if it slows below 100 kph, authorities race against time to save everyone on board.', 'https://image.tmdb.org/t/p/w500/qkTKtOHK9JEEOHgPQZ0dFtzs5ML.jpg', '2025-04-23', 6.8),
(6, 'In the Lost Lands', 'Action', 102, 'A queen sends the powerful and feared sorceress Gray Alys to the ghostly wilderness of the Lost Lands in search of a magical power, where she and her guide, the drifter Boyce, must outwit and outfight both man and demon.', 'https://image.tmdb.org/t/p/w500/dDlfjR7gllmr8HTeN6rfrYhTdwX.jpg', '2025-02-27', 6.3),
(7, 'Jewel Thief: The Heist Begins', 'Action', 118, 'In this high-octane battle of wits and wills, ingenious con artist Rehan devises a diamond heist while trying to outsmart Rajan, his sadistic adversary.', 'https://image.tmdb.org/t/p/w500/eujLbO0kf1eqWC8XpHUJdtAVW2J.jpg', '2025-04-25', 7.0),
(8, 'Sinners', 'Horror', 138, 'Trying to leave their troubled lives behind, twin brothers return to their hometown to start again, only to discover that an even greater evil is waiting to welcome them back.', 'https://image.tmdb.org/t/p/w500/jYfMTSiFFK7ffbY2lay4zyvTkEk.jpg', '2025-04-16', 7.6),
(9, 'The Monkey', 'Horror', 97, 'When twin brothers find a mysterious wind-up monkey, a series of outrageous deaths tear their family apart. Twenty-five years later, the monkey begins a new killing spree forcing the estranged brothers to confront the cursed toy.', 'https://image.tmdb.org/t/p/w500/yYa8Onk9ow7ukcnfp2QWVvjWYel.jpg', '2025-02-14', 5.9),
(10, 'Death of a Unicorn', 'Horror', 107, 'A father and daughter accidentally hit and kill a unicorn while en route to a weekend retreat, where his billionaire boss seeks to exploit the creature’s miraculous curative properties.', 'https://image.tmdb.org/t/p/w500/lXR32JepFwD1UHkplWqtBP1K79z.jpg', '2025-03-27', 6.2),
(11, 'Exterritorial', 'Thriller', 0, 'When her son vanishes inside a US consulate, ex-special forces soldier Sara does everything in her power to find him — and uncovers a dark conspiracy.', 'https://image.tmdb.org/t/p/w500/qWTfHG8KdXwr0bqypYbNZLenh0J.jpg', '2025-04-29', 6.6),
(12, 'Ash', 'Science Fiction', 95, 'A woman wakes up on a distant planet and finds the crew of her space station viciously killed. Her investigation into what happened sets in motion a terrifying chain of events.', 'https://image.tmdb.org/t/p/w500/5Oz39iyRuztiA8XqCNVDBuy2Ut3.jpg', '2025-03-20', 5.4),
(13, 'Thunderbolts*', 'Action', 127, 'After finding themselves ensnared in a death trap, seven disillusioned castoffs must embark on a dangerous mission that will force them to confront the darkest corners of their pasts.', 'https://image.tmdb.org/t/p/w500/hqcexYHbiTBfDIdDWxrxPtVndBX.jpg', '2025-04-30', 7.5),
(14, 'Conclave', 'Drama', 120, 'After the unexpected death of the Pope, Cardinal Lawrence is tasked with managing the covert and ancient ritual of electing a new one. Sequestered in the Vatican with the Catholic Church’s most powerful leaders until the process is complete, Lawrence finds himself at the center of a conspiracy that could lead to its downfall.', 'https://image.tmdb.org/t/p/w500/l4WXg5oQPK6GVlISKQNIUGb8rKJ.jpg', '2024-10-25', 7.2),
(15, 'G20', 'Action', 110, 'After the G20 Summit is overtaken by terrorists, President Danielle Sutton must bring all her statecraft and military experience to defend her family and her fellow leaders.', 'https://image.tmdb.org/t/p/w500/wv6oWAleCJZUk5htrGg413t3GCy.jpg', '2025-04-09', 6.6),
(16, 'Saint Catherine', 'Horror', 86, 'An orphaned girl is rescued from a satanic ritual and taken to Saint Catherine Institute for homeless youth. There she will learn new skills while facing demons that stalk her.', 'https://image.tmdb.org/t/p/w500/hBJdzKPeDaC96AzlrtMWBomYSZV.jpg', '2025-04-11', 8.2),
(17, 'Novocaine', 'Action', 110, 'When the girl of his dreams is kidnapped, everyman Nate turns his inability to feel pain into an unexpected strength in his fight to get her back.', 'https://image.tmdb.org/t/p/w500/xmMHGz9dVRaMY6rRAlEX4W0Wdhm.jpg', '2025-03-12', 6.9),
(18, 'Locked', 'Horror', 95, 'When Eddie breaks into a luxury SUV, he steps into a deadly trap set by William, a self-proclaimed vigilante delivering his own brand of twisted justice. With no means of escape, Eddie must fight to survive in a ride where escape is an illusion, survival is a nightmare, and justice shifts into high gear.', 'https://image.tmdb.org/t/p/w500/hhkiqXpfpufwxVrdSftzeKIANl3.jpg', '2025-03-20', 6.2),
(19, 'Drop', 'Mystery', 95, 'Violet, a widowed mother on her first date in years, arrives at an upscale restaurant where she is relieved that her date, Henry, is more charming and handsome than she expected. But their chemistry begins to curdle as Violet begins being irritated and then terrorized by a series of anonymous drops to her phone.', 'https://image.tmdb.org/t/p/w500/dS2S5lpfgRIRQOb7LDCjNsQqKjp.jpg', '2025-04-10', 6.7),
(20, 'The Accountant 2', 'Crime', 133, 'When an old acquaintance is murdered, Wolff is compelled to solve the case. Realizing more extreme measures are necessary, Wolff recruits his estranged and highly lethal brother, Brax, to help. In partnership with Treasury Agent Marybeth Medina, they uncover a deadly conspiracy, becoming targets of a ruthless network of killers who will stop at nothing to keep their secrets buried.', 'https://image.tmdb.org/t/p/w500/kMDUS7VmFhb2coRfVBoGLR8ADBt.jpg', '2025-04-23', 7.3),
(21, 'The Woman in the Yard', 'Horror', 87, 'In the aftermath of her husband\'s death, widow Ramona\'s struggle to raise her two kids is hindered by the arrival of a mysterious woman with supernatural abilities.', 'https://image.tmdb.org/t/p/w500/n0WS2TsNcS6dtaZKzxipyO7LuCJ.jpg', '2025-03-27', 6.2),
(22, 'Winter Spring Summer or Fall', 'Romance', 97, 'Remi and Barnes, two very different teenagers, meet by chance in the winter of their senior year, then spend four  days together over the course of a year that will change their lives forever.', 'https://image.tmdb.org/t/p/w500/mEPi5ISa5RmzpzeDfwkhX0CcNsf.jpg', '2024-12-27', 7.1),
(23, 'Until Dawn', 'Horror', 103, 'One year after her sister Melanie mysteriously disappeared, Clover and her friends head into the remote valley where she vanished in search of answers. Exploring an abandoned visitor center, they find themselves stalked by a masked killer and horrifically murdered one by one…only to wake up and find themselves back at the beginning of the same evening.', 'https://image.tmdb.org/t/p/w500/6O9nkcmZBymDXtxOGJmulqcxJdv.jpg', '2025-04-23', 6.5),
(24, 'The Sloth Lane', 'Animation', 90, 'After a terrifying storm destroys their home, a speedy sloth named Laura and her kooky family move to the big city in their rusted old food truck, Gordito, hoping to make their business a success. The family\'s delicious food catches the eye of a quick-witted cheetah who will stop at nothing to revive her failing fast-food business.', 'https://image.tmdb.org/t/p/w500/toBN1A9AnhoUzXyu9gaVyQVJehp.jpg', '2024-07-25', 6.3),
(25, 'Mickey 17', 'Science Fiction', 137, 'Unlikely hero Mickey Barnes finds himself in the extraordinary circumstance of working for an employer who demands the ultimate commitment to the job… to die, for a living.', 'https://image.tmdb.org/t/p/w500/edKpE9B5qN3e559OuMCLZdW1iBZ.jpg', '2025-02-28', 6.9),
(26, 'Cleaner', 'Action', 97, 'When a group of radical activists take over an energy company\'s annual gala, seizing 300 hostages, an ex-soldier turned window cleaner suspended 50 storeys up on the outside of the building must save those trapped inside, including her younger brother.', 'https://image.tmdb.org/t/p/w500/mwzDApMZAGeYCEVjhegKvCzDX0W.jpg', '2025-02-19', 6.6),
(27, 'Snow White', 'Family', 109, 'Following the benevolent King\'s disappearance, the Evil Queen dominated the once fair land with a cruel streak. Princess Snow White flees the castle when the Queen, in her jealousy over Snow White\'s inner beauty, tries to kill her. Deep into the dark woods, she stumbles upon seven magical dwarves and a young bandit named Jonathan. Together, they strive to survive the Queen\'s relentless pursuit and aspire to take back the kingdom in the process.', 'https://image.tmdb.org/t/p/w500/oLxWocqheC8XbXbxqJ3x422j9PW.jpg', '2025-03-19', 4.6),
(28, 'The Social Network', 'Drama', 121, 'In 2003, Harvard undergrad and computer programmer Mark Zuckerberg begins work on a new concept that eventually turns into the global social network known as Facebook. Six years later, Mark is one of the youngest billionaires ever, but his unprecedented success leads to both personal and legal complications when he ends up on the receiving end of two lawsuits, one involving his former friend.', 'https://image.tmdb.org/t/p/w500/n0ybibhJtQ5icDqTp8eRytcIHJx.jpg', '2010-10-01', 7.4),
(29, 'Carjackers', 'Action', 97, 'By day, they\'re invisible—valets, hostesses, and bartenders at a luxury hotel. By night, they\'re the Carjackers, a crew of skilled drivers who track and rob wealthy clients on the road. As they plan their ultimate heist, the hotel director hires a ruthless hitman, to stop them at all costs. With danger closing in, can Nora, Zoe, Steve, and Prestance pull off their biggest score yet?', 'https://image.tmdb.org/t/p/w500/wbkPMTz2vVai7Ujyp0ag7AM9SrO.jpg', '2025-03-27', 6.2),
(30, 'Home Sweet Home: Rebirth', 'Horror', 93, 'When a city is overrun with a demonically-possessed crowd, a cop must find the source of evil to save his family.', 'https://image.tmdb.org/t/p/w500/9rCBCm9cyI4JfLEhx3EncyncMR3.jpg', '2025-03-20', 6.5),
(31, 'Amadeus', 'History', 160, 'Disciplined Italian composer Antonio Salieri becomes consumed by jealousy and resentment towards the hedonistic and remarkably talented young Viennese composer Wolfgang Amadeus Mozart.', 'https://image.tmdb.org/t/p/w500/1n5VUlCqgmVax1adxGZm8oCFaKc.jpg', '1984-09-19', 8.0),
(32, 'iHostage', 'Thriller', 100, 'When a gunman enters an Apple Store in the heart of Amsterdam, the police face a delicate challenge to resolve the standoff.', 'https://image.tmdb.org/t/p/w500/tn3Ckxyc7OWXnhMPkiIBvqLdFC6.jpg', '2025-04-18', 6.1),
(33, 'Memoir of a Snail', 'Animation', 94, 'Forcibly separated from her twin brother when they are orphaned, a melancholic misfit learns how to find confidence within herself amid the clutter of misfortunes and everyday life.', 'https://image.tmdb.org/t/p/w500/57AgZv1ITeBLShiNdchZ5153evs.jpg', '2024-10-17', 8.0),
(34, 'Flow', 'Animation', 85, 'A solitary cat, displaced by a great flood, finds refuge on a boat with various species and must navigate the challenges of adapting to a transformed world together.', 'https://image.tmdb.org/t/p/w500/imKSymKBK7o73sajciEmndJoVkR.jpg', '2024-08-29', 8.2),
(35, 'The Amateur', 'Thriller', 123, 'After his life is turned upside down when his wife is killed in a London terrorist attack, a brilliant but introverted CIA decoder takes matters into his own hands when his supervisors refuse to take action.', 'https://image.tmdb.org/t/p/w500/SNEoUInCa5fAgwuEBMIMBGvkkh.jpg', '2025-04-05', 6.7),
(36, 'Cocoon: Aru Natsu no Shoujo-tachi yori', 'Animation', 60, 'San and Mayu are two schoolgirls living in Okinawa during the closing months of the Pacific War. Together with their classmates, the two friends are drafted into the war effort as nurses for wounded soldiers. When ordered to die for their country, the remaining members of the group escape only to face the harsh environment of a tropical paradise that has become a hellish battlefield.', 'https://image.tmdb.org/t/p/w500/mwTk1RsRCJNPacc0X3PWsxuHzQv.jpg', '2025-03-30', 6.5),
(37, 'Dog Man', 'Family', 89, 'When a faithful police dog and his human police officer owner are injured together on the job, a harebrained but life-saving surgery fuses the two of them together and Dog Man is born. Dog Man is sworn to protect and serve—and fetch, sit and roll over. As Dog Man embraces his new identity and strives to impress his Chief, he must stop the pretty evil plots of feline supervillain Petey the Cat.', 'https://image.tmdb.org/t/p/w500/89wNiexZdvLQ41OQWIsQy4O6jAQ.jpg', '2025-01-24', 7.8),
(38, 'Fight or Flight', 'Action', 101, 'A mercenary takes on the job of tracking down a target on a plane but must protect her when they\'re surrounded by people trying to kill both of them.', 'https://image.tmdb.org/t/p/w500/x4nWnfgJvL045rcUCSJzfgIIY9i.jpg', '2025-04-03', 5.7),
(39, 'Shrek', 'Animation', 90, 'It ain\'t easy bein\' green -- especially if you\'re a likable (albeit smelly) ogre named Shrek. On a mission to retrieve a gorgeous princess from the clutches of a fire-breathing dragon, Shrek teams up with an unlikely compatriot -- a wisecracking donkey.', 'https://image.tmdb.org/t/p/w500/EKERKucZeBrkrR8xLDTkr5FeDc.jpg', '2001-05-18', 7.8),
(40, 'Peter Pan\'s Neverland Nightmare', 'Horror', 89, 'Wendy Darling strikes out in an attempt to rescue her brother Michael from the clutches of the evil Peter Pan who intends to send him to Neverland. Along the way she meets a twisted Tinkerbell, who is hooked on what she thinks is fairy dust.', 'https://image.tmdb.org/t/p/w500/mOR1Ks0EcXocwMV4EPv4letz0F5.jpg', '2025-01-13', 4.9),
(41, 'Warfare', 'War', 96, 'A platoon of Navy SEALs embarks on a dangerous mission in Ramadi, Iraq, with the chaos and brotherhood of war retold through their memories of the event.', 'https://image.tmdb.org/t/p/w500/oXsDTDt1MqxJreROd2cbd2FMOZ2.jpg', '2025-04-09', 7.3),
(42, 'Humanist Vampire Seeking Consenting Suicidal Person', 'Romance', 91, 'Sasha is a young vampire with a serious problem: she\'s too sensitive to kill. When her exasperated parents cut off her blood supply, Sasha\'s life is in jeopardy. Luckily, she meets Paul, a lonely teenager with suicidal tendencies who is willing to give his life to save hers. But their friendly agreement soon becomes a nocturnal quest to fulfill Paul\'s last wishes before day breaks.', 'https://image.tmdb.org/t/p/w500/m5OItLBY5T38ew1YI4VSIXjl5G2.jpg', '2023-10-13', 7.4),
(43, 'Ne Zha 2', 'Animation', 144, 'Following the Tribulation, although the souls of Ne Zha and Ao Bing were preserved, their physical bodies will soon be destroyed. Tai Yi Zhen Ren plans to use the Seven Colored Lotus to reshape their physical forms, but encounters numerous difficulties. What will become of Ne Zha and Ao Bing?', 'https://image.tmdb.org/t/p/w500/5lUmWTGkEcYnXujixXn31o9q2T0.jpg', '2025-01-29', 7.9),
(44, 'Freaky Tales', 'Action', 107, 'In 1987 Oakland, a mysterious force guides The Town\'s underdogs in four interconnected tales: teen punks defend their turf against Nazi skinheads, a rap duo battles for hip-hop immortality, a weary henchman gets a shot at redemption, and an NBA All-Star settles the score.', 'https://image.tmdb.org/t/p/w500/lo5mfglsGeMYmAkSRXs9DATNJxk.jpg', '2025-04-04', 6.8),
(45, 'I, the Executioner', 'Action', 118, 'The veteran detective Seo Do-cheol and his team at Major Crimes, relentless in their pursuit of criminals, join forces with rookie cop Park Sun-woo to track down a serial killer who has plunged the nation into turmoil.', 'https://image.tmdb.org/t/p/w500/gAtP0usArK5gVOBObnsENKLwML8.jpg', '2024-09-13', 7.0),
(46, 'Neighborhood Watch', 'Crime', 92, 'When a mentally ill young man thinks he witnesses an abduction and the police refuse to believe him, he reluctantly turns to his next door neighbor – a bitter, retired security guard – to help him find the missing woman.', 'https://image.tmdb.org/t/p/w500/dtSYJ20Qy1yLM9KTnv2SUEfxekO.jpg', '2025-04-25', 6.6),
(47, 'Pride & Prejudice', 'Drama', 127, 'A story of love and life among the landed English gentry during the Georgian era. Mr. Bennet is a gentleman living in Hertfordshire with his overbearing wife and five daughters, but if he dies their house will be inherited by a distant cousin whom they have never met, so the family\'s future happiness and security is dependent on the daughters making good marriages.', 'https://image.tmdb.org/t/p/w500/sGjIvtVvTlWnia2zfJfHz81pZ9Q.jpg', '2005-09-16', 8.1),
(48, '825 Forest Road', 'Horror', 101, 'After a family tragedy, Chuck Wilson hopes to start a new life in Ashland Falls with his wife Maria and little sister Elizabeth, but he quickly discovers that the town has a dark history of being haunted by a ghostly woman who drives residents to suicide.', 'https://image.tmdb.org/t/p/w500/ohESp5Nw49OD4ExCeNYCEIGX2iq.jpg', '2025-04-24', 6.1),
(49, 'Alarum', 'Action', 95, 'Two married spies caught in the crosshairs of an international intelligence network will stop at nothing to obtain a critical asset. Joe and Lara are agents living off the grid whose quiet retreat at a winter resort is blown to shreds when members of the old guard suspect the two may have joined an elite team of rogue spies, known as Alarum.', 'https://image.tmdb.org/t/p/w500/v313aUGmMNj6yNveaiQXysBmjVS.jpg', '2025-01-16', 5.8),
(50, 'Limonov: The Ballad', 'History', 133, 'A revolutionary militant, a thug, an underground writer, a butler to a millionaire in Manhattan. But also a switchblade-waving poet, a lover of beautiful women, a warmonger, a political agitator, and a novelist who wrote of his greatness. Eduard Limonov’s life story is a journey through Russia, America, and Europe during the second half of the 20th century.', 'https://image.tmdb.org/t/p/w500/oECqqtEb21qsVJangfa5F7Mdods.jpg', '2024-09-05', 6.4),
(51, 'The Alto Knights', 'Crime', 123, 'Two of New York\'s most notorious organized crime bosses, Frank Costello and Vito Genovese, vie for control of the city\'s streets. Once the best of friends, petty jealousies and a series of betrayals place them on a deadly collision course that will reshape the Mafia (and America) forever.', 'https://image.tmdb.org/t/p/w500/95KmR0xNuZZ6DNESDwLKWGIBvMg.jpg', '2025-03-14', 6.1),
(52, 'Love Hurts', 'Action', 83, 'A realtor is pulled back into the life he left behind after his former partner-in-crime resurfaces with an ominous message. With his crime-lord brother also on his trail, he must confront his past and the history he never fully buried.', 'https://image.tmdb.org/t/p/w500/skPPVeHoTTVVSJlb0Ib5vrqiuA4.jpg', '2025-02-06', 5.9),
(53, 'Star Wars: Episode III - Revenge of the Sith', 'Adventure', 140, 'The evil Darth Sidious enacts his final plan for unlimited power -- and the heroic Jedi Anakin Skywalker must choose a side.', 'https://image.tmdb.org/t/p/w500/xfSAoBEm9MNBjmlNcDYLvLSMlnq.jpg', '2005-05-17', 7.4),
(54, 'Popeye the Slayer Man', 'Horror', 88, 'A curious group of friends sneak into an abandoned spinach canning factory to film a documentary on the legend of the \"Sailor Man,\" who is said to haunt the factory and local docks.', 'https://image.tmdb.org/t/p/w500/nVwu3mN7hr1yF467pGct3yQFM41.jpg', '2025-03-21', 6.0),
(55, 'Black Bag', 'Thriller', 94, 'When intelligence agent Kathryn Woodhouse is suspected of betraying the nation, her husband – also a legendary agent – faces the ultimate test of whether to be loyal to his marriage, or his country.', 'https://image.tmdb.org/t/p/w500/hHPovtU4b96LHcoeEwRkGHI5btw.jpg', '2025-03-12', 6.3),
(56, 'Last Breath', 'Thriller', 93, 'Seasoned deep-sea divers battle the raging elements to rescue their crewmate trapped hundreds of feet below the ocean\'s surface.', 'https://image.tmdb.org/t/p/w500/w04Xg5Bnqj7ECdCxTsHgqK6AtJW.jpg', '2025-02-27', 6.9),
(57, 'Old Guy', 'Action', 93, 'Stuck at a dead end but vying for the love of spunky club manager Anata, aging contract killer Danny Dolinski is thrilled when The Company pulls him back in the field, but only to train Gen Z newcomer Wihlborg, a prodigy assassin with an attitude.', 'https://image.tmdb.org/t/p/w500/ulVVfNY8bmy1RbHfY4zi3fOwGzh.jpg', '2024-12-13', 5.8),
(58, 'Meet the Khumalos', 'Comedy', 92, 'Two moms — once best friends, now arch-enemies — start a neighborhood war against each other when they find out their kids are head-over-heels in love.', 'https://image.tmdb.org/t/p/w500/dEZHHgL1g4oA6OCntHGudDIgQlb.jpg', '2025-04-10', 5.3),
(59, 'The Day the Earth Blew Up: A Looney Tunes Movie', 'Family', 90, 'Porky and Daffy, the classic animated odd couple, turn into unlikely heroes when their antics at the local bubble gum factory uncover a secret alien mind control plot. Against all odds, the two are determined to save their town (and the world!)...that is if they don\'t drive each other crazy in the process.', 'https://image.tmdb.org/t/p/w500/s2lB1kaYCdGSnZX5meQCiOR6HfX.jpg', '2024-08-01', 7.6),
(60, 'How to Tame a Silver Fox', 'Romance', 80, 'Harper Reeves is just trying to finish up her senior year of Yale without being a completely friendless loser. However, when her party\'s suddenly crashed by her dad\'s best friend and mysterious business partner, Chris Collins, she realizes she\'d much rather have been busted by the cops. Chris, always in over-protective mode, gets on Harper\'s nerves, until she realizes he needs to go. Together with her best friend Maria, she brainstorms Operation Seduction, planning to get Chris to fall in love with her so her dad will kick him out himself. But every time Chris saves Harper from trouble, she gets closer to realizing she may have feelings for him after all.', 'https://image.tmdb.org/t/p/w500/xs5ajCIA4ElplR1Om9dyNKSQXWr.jpg', '2025-04-07', 7.3),
(61, 'Attack on Titan: THE LAST ATTACK', 'Animation', 145, 'A colossal-sized omnibus film bringing together the last two episodes of Attack on Titan in the franchise\'s first-ever theatrical experience. After venturing beyond the walls and separated from his comrades, Eren finds himself inspired by this new truth and plots the \"Rumbling,\" a terrifying plan to eradicate every living thing in the world. With the fate of the world hanging in the balance, a motley crew of Eren\'s former comrades and enemies scramble to halt his deadly mission. The only question is, can they stop him?', 'https://image.tmdb.org/t/p/w500/wgwldDDlTDDMrluOMkpSA8lyKjv.jpg', '2024-11-08', 8.6),
(62, 'Presence', 'Horror', 84, 'A couple and their children move into a seemingly normal suburban home. When strange events occur, they begin to believe there is something else in the house with them. The presence is about to disrupt their lives in unimaginable ways.', 'https://image.tmdb.org/t/p/w500/hZ8dTeBzigV5SVgwG1ikSROAFiS.jpg', '2025-01-16', 6.0),
(63, 'Pulp Fiction', 'Thriller', 154, 'A burger-loving hit man, his philosophical partner, a drug-addled gangster\'s moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.', 'https://image.tmdb.org/t/p/w500/vQWk5YBFWF4bZaofAbv0tShwBvQ.jpg', '1994-09-10', 8.5),
(64, 'The King of Kings', 'Animation', 104, 'Charles Dickens tells his young son Walter the greatest story ever told, and what begins as a bedtime tale becomes a life-changing journey. Through vivid imagination, the boy walks alongside Jesus, witnessing His miracles, facing His trials, and understanding His ultimate sacrifice.', 'https://image.tmdb.org/t/p/w500/ccw7CCIAvcZV431CP7NhHAHuiHR.jpg', '2025-04-07', 7.7),
(65, 'A Complete Unknown', 'Drama', 140, 'New York, early 1960s. Against the backdrop of a vibrant music scene and tumultuous cultural upheaval, an enigmatic 19-year-old from Minnesota arrives in the West Village with his guitar and revolutionary talent, destined to change the course of American music.', 'https://image.tmdb.org/t/p/w500/llWl3GtNoXosbvYboelmoT459NM.jpg', '2024-12-18', 7.2),
(66, 'Paddington in Peru', 'Family', 106, 'Paddington travels to Peru to visit his beloved Aunt Lucy, who now resides at the Home for Retired Bears. With the Brown Family in tow, a thrilling adventure ensues when a mystery plunges them into an unexpected journey through the Amazon rainforest and up to the mountain peaks of Peru.', 'https://image.tmdb.org/t/p/w500/rzfqeLdHIysJGrspMICyedpqDqt.jpg', '2024-11-08', 6.8),
(67, 'Absolution', 'Action', 112, 'An aging ex-boxer gangster working as muscle for a Boston crime boss receives an upsetting diagnosis.  Despite a faltering memory, he attempts to rectify the sins of his past and reconnect with his estranged children. He is determined to leave a positive legacy for his grandson, but the criminal underworld isn’t done with him and won’t loosen their grip willingly.', 'https://image.tmdb.org/t/p/w500/gt70JOD9xsPlpJnuBJAWdOT4yRg.jpg', '2024-10-31', 5.9),
(68, 'Devara: Part 1', 'Action', 175, 'Devara, a fearless man from a coastal region, embarks on a perilous journey into the treacherous world of the sea to safeguard the lives of his people. Unbeknownst to him, his brother Bhaira is plotting a conspiracy against him. As events unfold, Devara passes on his legacy to his mild-mannered and timid son, Varada.', 'https://image.tmdb.org/t/p/w500/hdpYUidbB83AfemP369W7guOIlr.jpg', '2024-09-26', 7.0),
(69, 'The Brutalist', 'Drama', 215, 'When a visionary architect and his wife flee post-war Europe in 1947 to rebuild their legacy and witness the birth of modern United States, their lives are changed forever by a mysterious, wealthy client.', 'https://image.tmdb.org/t/p/w500/vP7Yd6couiAaw9jgMd5cjMRj3hQ.jpg', '2024-12-20', 7.1),
(70, 'Terrifier 3', 'Horror', 125, 'Five years after surviving Art the Clown\'s Halloween massacre, Sienna and Jonathan are still struggling to rebuild their shattered lives. As the holiday season approaches, they try to embrace the Christmas spirit and leave the horrors of the past behind. But just when they think they\'re safe, Art returns, determined to turn their holiday cheer into a new nightmare. The festive season quickly unravels as Art unleashes his twisted brand of terror, proving that no holiday is safe.', 'https://image.tmdb.org/t/p/w500/ju10W5gl3PPK3b7TjEmVOZap51I.jpg', '2024-10-09', 6.8),
(71, 'Opus', 'Horror', 104, 'A young writer is invited to the remote compound of a legendary pop star who mysteriously disappeared thirty years ago. Surrounded by the star\'s cult of sycophants and intoxicated journalists, she finds herself in the middle of his twisted plan.', 'https://image.tmdb.org/t/p/w500/m0du9dsiOVeb0SgfqR8ZAEPRxww.jpg', '2025-03-13', 5.4),
(72, 'Heretic', 'Horror', 111, 'Two young missionaries are forced to prove their faith when they knock on the wrong door and are greeted by a diabolical Mr. Reed, becoming ensnared in his deadly game of cat-and-mouse.', 'https://image.tmdb.org/t/p/w500/fr96XzlzsONrQrGfdLMiwtQjott.jpg', '2024-10-31', 7.1),
(73, 'Titanic: The Digital Resurrection', 'Documentary', 69, 'Using cutting-edge scanning technology and state-of-the-art CGI, a team of experts creates the first high-resolution 3D digital twin of the Titanic wreck. Through a groundbreaking immersive investigation, they uncover the ship’s final moments, shedding light on the acts of heroism and cowardice aboard—and revealing the true story behind the sinking of the “unsinkable” ship.', 'https://image.tmdb.org/t/p/w500/mJNpxvtUNRVOfhFwukNewSf3gkf.jpg', '2025-04-12', 8.5),
(74, 'Better Man', 'Music', 135, 'Follow Robbie Williams\' journey from childhood, to being the youngest member of chart-topping boyband Take That, through to his unparalleled achievements as a record-breaking solo artist – all the while confronting the challenges that stratospheric fame and success can bring.', 'https://image.tmdb.org/t/p/w500/fbGCmMp0HlYnAPv28GOENPShezM.jpg', '2024-12-06', 7.8),
(75, 'The Life List', 'Romance', 123, 'When her mother sends her on a quest to complete a teenage bucket list, a young woman uncovers family secrets, finds romance — and rediscovers herself.', 'https://image.tmdb.org/t/p/w500/5fg98cVo7da7OIK45csdLSd4NaU.jpg', '2025-03-27', 6.8),
(76, 'Detective Chinatown 1900', 'Comedy', 135, 'In 1900, a white woman was murdered in Chinatown in San Francisco, and the suspect was a Chinese man. The murder caused social shock, and people demanded the closure of Chinatown.', 'https://image.tmdb.org/t/p/w500/g3GsgIlH3fA4RxhNOAMvSbVWyfW.jpg', '2025-01-29', 6.1),
(77, 'The Mummy', 'Adventure', 124, 'Dashing legionnaire Rick O\'Connell stumbles upon the hidden ruins of Hamunaptra while in the midst of a battle to claim the area in 1920s Egypt. It has been over three thousand years since former High Priest Imhotep suffered a fate worse than death as a punishment for a forbidden love—along with a curse that guarantees eternal doom upon the world if he is ever awoken.', 'https://image.tmdb.org/t/p/w500/yhIsVvcUm7QxzLfT6HW2wLf5ajY.jpg', '1999-04-16', 6.9),
(79, 'Final Destination', 'Horror', 98, 'After a teenager has a terrifying vision of him and his friends dying in a plane crash, he prevents the accident only to have Death hunt them down, one by one.', 'https://image.tmdb.org/t/p/w500/1mXhlQMnlfvJ2frxTjZSQNnA9Vp.jpg', '2000-03-17', 6.6),
(80, 'Oh, Canada', 'Drama', 94, 'Famed Canadian-American leftist documentary filmmaker Leonard Fife was one of sixty thousand draft evaders and deserters who fled to Canada to avoid serving in Vietnam. Now in his late seventies, Fife is dying of cancer in Montreal and has agreed to a final interview in which he is determined to bare all his secrets at last, to demythologize his mythologized life.', 'https://image.tmdb.org/t/p/w500/hKDR5AYXRTPb751oc7xK2Ay3PH0.jpg', '2024-12-06', 6.0),
(81, 'Werewolves', 'Action', 94, 'A year after a supermoon’s light activated a dormant gene, transforming humans into bloodthirsty werewolves and causing nearly a billion deaths, the nightmare resurfaces as the supermoon rises again. Two scientists attempt to stop the mutation but fail and must now struggle to reach one of their family homes.', 'https://image.tmdb.org/t/p/w500/otXBlMPbFBRs6o2Xt6KX59Qw6dL.jpg', '2024-12-04', 6.2),
(82, 'The Penguin Lessons', 'Drama', 111, 'In 1976, as Argentina descends into violence and chaos, a world-weary English teacher regains his compassion for others thanks to an unlikely friendship with a penguin.', 'https://image.tmdb.org/t/p/w500/hZ7rDX01j86x8O1E7Pe7658QYs4.jpg', '2025-03-27', 7.2),
(83, 'Dalia and the Red Book', 'Adventure', 106, 'Dalia, the daughter of a famous writer who has recently died, inherits the legacy of finishing his book. To do this, Dalia will become part of the book and will come face to face with the characters who have taken the theme of the book in order to be the protagonists.', 'https://image.tmdb.org/t/p/w500/yXhYGfEIeZ3kKTXMuquAhWpo514.jpg', '2024-10-17', 7.3),
(84, 'Pangolin: Kulu\'s Journey', 'Documentary', 90, 'Rescued from poachers, an endangered baby pangolin embarks on a journey back to the wild with help from a devoted human guardian in this documentary.', 'https://image.tmdb.org/t/p/w500/7Ky1cTLJzOk8or25YGEvw8S6oxI.jpg', '2025-04-21', 7.3),
(85, 'Madagascar', 'Family', 86, 'Four animal friends get a taste of the wild life when they break out of captivity at the Central Park Zoo and wash ashore on the island of Madagascar.', 'https://image.tmdb.org/t/p/w500/zMpJY5CJKUufG9OTw0In4eAFqPX.jpg', '2005-05-25', 6.9),
(86, 'Emilia Pérez', 'Drama', 132, 'Rita, an underrated lawyer working for a large law firm more interested in getting criminals out of jail than bringing them to justice, is hired by the leader of a criminal organization.', 'https://image.tmdb.org/t/p/w500/fko8fTfL6BcAqOUh6BZYUjt4SQP.jpg', '2024-08-21', 6.7),
(87, 'The Bayou', 'Thriller', 87, 'Vacation turns disaster when Houston grad Kyle and her friends survive a plane crash in the desolate Louisiana everglades, only to discover there\'s something way more dangerous lurking in the shallows.', 'https://image.tmdb.org/t/p/w500/sf6j1SbgDf7VTjL1MRq5MAQSOyE.jpg', '2025-01-31', 6.3),
(88, 'Bridget Jones: Mad About the Boy', 'Romance', 124, 'Bridget Jones navigates life as a widow and single mum with the help of her family, friends, and former lover, Daniel. Back to work and on the apps, she\'s pursued by a younger man and maybe – just maybe – her son\'s science teacher.', 'https://image.tmdb.org/t/p/w500/taEVBdVSqYo9YeN3ycw2SosklZL.jpg', '2025-02-12', 6.7),
(89, 'I\'m Still Here', 'Drama', 138, 'In 1971, military dictatorship in Brazil reaches its height. The Paiva family — Rubens, Eunice, and their five children — live in a beachside house in Rio, open to all their friends. One day, Rubens is taken for questioning and does not return.', 'https://image.tmdb.org/t/p/w500/gZnsMbhCvhzAQlKaVpeFRHYjGyb.jpg', '2024-09-19', 8.0),
(90, 'We Live in Time', 'Romance', 108, 'An up-and-coming chef and a recent divorcée find their lives forever changed when a chance encounter brings them together, in a decade-spanning, deeply moving romance.', 'https://image.tmdb.org/t/p/w500/oeDNBgnlGF6rnyX1P1K8Vl2f3lW.jpg', '2024-10-10', 7.3),
(91, 'One of Them Days', 'Comedy', 97, 'Best friends and roommates Dreux and Alyssa are about to have One of Them Days. When they discover Alyssa’s boyfriend has blown their rent money, the duo finds themselves going to extremes in a comical race against the clock to avoid eviction and keep their friendship intact.', 'https://image.tmdb.org/t/p/w500/ccn6bFUA5DECjA3Lo0CuJqGNQCv.jpg', '2025-01-16', 6.8),
(92, 'The Shrouds', 'Drama', 119, 'Inconsolable since the death of his wife, Karsh, a prominent businessman, invents a revolutionary and controversial technology that enables the living to monitor their dear departed in their shrouds. One night, multiple graves, including that of Karsh’s wife, are desecrated, and he sets out to track down the perpetrators.', 'https://image.tmdb.org/t/p/w500/6J4lPto3rjlHZwhIhCy9cOWNnqb.jpg', '2025-03-20', 6.1),
(93, 'Queer', 'Drama', 136, '1950. William Lee, an American expat in Mexico City, spends his days almost entirely alone, except for a few contacts with other members of the small American community. His encounter with Eugene Allerton, an expat former soldier, new to the city, shows him, for the first time, that it might be finally possible to establish an intimate connection with somebody.', 'https://image.tmdb.org/t/p/w500/xe4b2TMciLKA1C0JlhWxb4ENLln.jpg', '2024-11-27', 6.7),
(94, 'Seven Veils', 'Drama', 107, 'Jeanine, an earnest theatre director, has been given the task of remounting her former mentor’s most famous work, the opera Salome. Haunted by dark and disturbing memories from her past, she allows her repressed trauma to color the present as she re-enters the opera world after so many years away.', 'https://image.tmdb.org/t/p/w500/1IqDhi52gO7cQAnDnr6mOXqfKJb.jpg', '2025-03-07', 6.0),
(95, 'The Notebook', 'Romance', 123, 'An epic love story centered around an older man who reads aloud to a woman with Alzheimer\'s. From a faded notebook, the old man\'s words bring to life the story about a couple who is separated by World War II, and is then passionately reunited, seven years later, after they have taken different paths.', 'https://image.tmdb.org/t/p/w500/rNzQyW4f8B8cQeg7Dgj3n6eT5k9.jpg', '2004-05-25', 7.9),
(96, 'M3GAN', 'Science Fiction', 102, 'A brilliant toy company roboticist uses artificial intelligence to develop M3GAN, a life-like doll programmed to emotionally bond with her newly orphaned niece. But when the doll\'s programming works too well, she becomes overprotective of her new friend with terrifying results.', 'https://image.tmdb.org/t/p/w500/d9nBoowhjiiYc4FBNtQkPY7c11H.jpg', '2022-12-28', 7.1),
(97, 'Armor', 'Action', 89, 'Armored truck security guard James Brody is working with his son Casey transporting millions of dollars between banks when a team of thieves led by Rook orchestrate a takeover of their truck to seize the riches. Following a violent car chase, Rook soon has the armored truck surrounded and James and Casey find themselves cornered onto a decrepit bridge.', 'https://image.tmdb.org/t/p/w500/pnXLFioDeftqjlCVlRmXvIdMsdP.jpg', '2024-10-30', 5.7),
(98, 'Parthenope', 'Romance', 137, 'Parthenope, born in the sea near Naples in 1950, is beautiful, enigmatic, and intelligent. She is shamelessly courted by many. However, beauty comes at a cost.', 'https://image.tmdb.org/t/p/w500/uhMIPArCsAzMKRMtLj2VDqAd6ZO.jpg', '2024-10-19', 6.8),
(99, 'Magazine Dreams', 'Drama', 124, 'Aspiring bodybuilder Killian Maddox struggles to find human connection in this exploration of celebrity and violence. Nothing deters him from his fiercely protected dream of superstardom, not even the doctors who warn him of the permanent damage he causes to himself with his quest.', 'https://image.tmdb.org/t/p/w500/vSidx9NHKuQIPEQ4yI9b9zkTeh3.jpg', '2025-03-21', 6.3),
(100, 'The Friend', 'Drama', 120, 'When a solitary writer adopts and bonds with a Great Dane that belonged to a late friend, she begins to come to terms with her past and her own creative inner life.', 'https://image.tmdb.org/t/p/w500/zGgycGRhPbP54omtSLwPJlUGWUk.jpg', '2025-03-28', 6.1),
(101, 'Avengers: Infinity War', 'Action', 148, 'As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.', 'https://image.tmdb.org/t/p/w500/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg', '2018-04-25', 8.2),
(102, 'Final Destination Bloodlines', 'Horror', 110, 'Plagued by a violent recurring nightmare, college student Stefanie heads home to track down the one person who might be able to break the cycle and save her family from the grisly demise that inevitably awaits them all.', 'https://image.tmdb.org/t/p/w500/6WxhEvFsauuACfv8HyoVX6mZKFj.jpg', '2025-05-09', 7.3),
(103, 'Last Bullet', 'Action', 111, 'Car genius Lino returns to conclude his vendetta against Areski and the corrupt commander who ruined their lives in this turbo-charged trilogy finale.', 'https://image.tmdb.org/t/p/w500/qycPITRqXgPai7zj1gKffjCdSB5.jpg', '2025-05-06', 6.8),
(104, 'Bambi: A Life in the Woods', 'Adventure', 77, 'The life of Bambi, a male roe deer, from his birth through childhood, the loss of his mother, the finding of a mate, the lessons he learns from his father, and the experience he gains about the dangers posed by human hunters in the forest.', 'https://image.tmdb.org/t/p/w500/vWNVHtwOhcoOEUSrY1iHRGbgH8O.jpg', '2024-10-16', 6.1),
(105, 'Rust', 'Western', 0, 'Infamous outlaw Harland Rust breaks his estranged grandson Lucas out of prison, after Lucas is convicted to hang for an accidental murder. The two must outrun legendary U.S Marshal Wood Helm and bounty hunter Fenton \"Preacher\" Lang who are hot on their tails. Deeply buried secrets rise from the ashes and an unexpected familial bond begins to form as the mismatched duo tries to survive the merciless American Frontier.', 'https://image.tmdb.org/t/p/w500/tbJ3RkA2s6X5qrBzrYHYTxvDBui.jpg', '2025-05-01', 6.4),
(106, 'Karate Kid: Legends', 'Action', 94, 'After a family tragedy, kung fu prodigy Li Fong is uprooted from his home in Beijing and forced to move to New York City with his mother. When a new friend needs his help, Li enters a karate competition – but his skills alone aren\'t enough. Li\'s kung fu teacher Mr. Han enlists original Karate Kid Daniel LaRusso for help, and Li learns a new way to fight, merging their two styles into one for the ultimate martial arts showdown.', 'https://image.tmdb.org/t/p/w500/ckI6bmQDvvGy20FPTIW1kfGKGRK.jpg', '2025-05-08', 7.4),
(107, 'Mission: Impossible - The Final Reckoning', 'Action', 170, 'Ethan Hunt and the IMF team continue their search for the terrifying AI known as the Entity — which has infiltrated intelligence networks all over the globe — with the world\'s governments and a mysterious ghost from Ethan\'s past on their trail. Joined by new allies and armed with the means to shut the Entity down for good, Hunt is in a race against time to prevent the world as we know it from changing forever.', 'https://image.tmdb.org/t/p/w500/z53D72EAOxGRqdr7KXXWp9dJiDe.jpg', '2025-05-17', 7.8),
(108, 'Nonnas', 'Comedy', 114, 'After losing his beloved mother, a man risks everything to honor her by opening an Italian restaurant with actual nonnas — grandmothers — as the chefs.', 'https://image.tmdb.org/t/p/w500/5Bf1xlxLuCHdi74MpdFi2Yz3FGJ.jpg', '2025-05-01', 6.7),
(109, 'Desert Dawn', 'Action', 89, 'A newly appointed small-town sheriff and his begrudging deputy get tangled up in a web of lies and corruption involving shady businessmen and the cartel while investigating the murder of a mysterious woman.', 'https://image.tmdb.org/t/p/w500/S21BfLrJSD9njucBfY3CKqp8rD.jpg', '2025-05-15', 9.0),
(110, 'Lilo & Stitch', 'Family', 108, 'The wildly funny and touching story of a lonely Hawaiian girl and the fugitive alien who helps to mend her broken family.', 'https://image.tmdb.org/t/p/w500/3bN675X0K2E5QiAZVChzB5wq90B.jpg', '2025-05-21', 0.0),
(111, 'Flight Risk', 'Action', 91, 'A U.S. Marshal escorts a government witness to trial after he\'s accused of getting involved with a mob boss, only to discover that the pilot who is transporting them is also a hitman sent to assassinate the informant. After they subdue him, they\'re forced to fly together after discovering that there are others attempting to eliminate them.', 'https://image.tmdb.org/t/p/w500/q0bCG4NX32iIEsRFZqRtuvzNCyZ.jpg', '2025-01-22', 6.1),
(112, 'The Accountant²', 'Crime', 133, 'When an old acquaintance is murdered, Wolff is compelled to solve the case. Realizing more extreme measures are necessary, Wolff recruits his estranged and highly lethal brother, Brax, to help. In partnership with Marybeth Medina, they uncover a deadly conspiracy, becoming targets of a ruthless network of killers who will stop at nothing to keep their secrets buried.', 'https://image.tmdb.org/t/p/w500/ieYaJz2nzs4wcqpWaofagzGoGPi.jpg', '2025-04-23', 7.1),
(113, 'Karol G: Tomorrow Was Beautiful', 'Music', 108, 'Karol G pulls back the curtain on her rise to stardom while navigating a sold-out stadium tour, creating new music and releasing a new album.', 'https://image.tmdb.org/t/p/w500/5aXoQYwaQ7JJVUWclHAEXJgiS2M.jpg', '2025-04-30', 7.9),
(114, 'The Ugly Stepsister', 'Horror', 109, 'In a fairy-tale kingdom where beauty is a brutal business, Elvira battles to compete with her incredibly beautiful stepsister, and she will go to any length to catch the prince’s eye.', 'https://image.tmdb.org/t/p/w500/crX9rSg9EohybhkEe8iTQUCe55y.jpg', '2025-03-07', 6.6),
(115, 'Gunslingers', 'Western', 104, 'When the most wanted man in America surfaces in a small Kentucky town, his violent history -- and a blood-thirsty mob seeking vengeance and a king’s ransom -- soon follow. As brothers face off against one another and bullets tear the town to shreds, this lightning-fast gunslinger makes his enemies pay the ultimate price for their greed.', 'https://image.tmdb.org/t/p/w500/O7REXWPANWXvX2jhQydHjAq2DV.jpg', '2025-04-11', 6.5),
(116, 'Vini Jr.', 'Documentary', 106, 'Vini Jr. has it all: talent, resilience and boldness. Follow his dancing, unpredictable feet on his inspiring journey to becoming a global soccer star.', 'https://image.tmdb.org/t/p/w500/4ImGMcwpy4k2zmcXBSFYNK6kRr9.jpg', '2025-05-15', 1.9),
(117, 'Hurry Up Tomorrow', 'Thriller', 106, 'A musician plagued by insomnia is pulled into an odyssey with a stranger who begins to unravel the very core of his existence.', 'https://image.tmdb.org/t/p/w500/9W44XSMYbjXdlXcQhD104zPhhhJ.jpg', '2025-05-14', 5.6),
(118, 'A Deadly American Marriage', 'Documentary', 103, 'Murder or self-defense? Told from both sides, this documentary explores the killing of Jason Corbett during a dispute with his wife and her father.', 'https://image.tmdb.org/t/p/w500/2DiydymXoLD9KUGZKorBNapshl1.jpg', '2025-05-09', 6.7),
(119, 'Marcello Mio', 'Comedy', 121, 'Pressured from all sides by the figure of her father, Chiara Mastroianni decides to bring him back to life through her own self. She goes by the name of Marcello, dresses like him and asks to now be considered an actor, not an actress. The people around her believe this to be a temporary joke, but Chiara is determined not to give up her new identity…', 'https://image.tmdb.org/t/p/w500/gr3oOAUYR4ttdYPfd1Nr9bUMCf5.jpg', '2024-05-21', 5.8),
(120, 'Ligaw', 'Drama', 85, 'A sultry woman who charms the hearts of men in a remote province. Dolores, a devoted wife to her paraplegic husband, falls for a young mountaineer. What begins as a reckless escape turns into an affair drenched in passion and secrecy.', 'https://image.tmdb.org/t/p/w500/yc8V3KEnhvPrzXpdYTXdTvjpOb5.jpg', '2025-05-09', 0.0),
(121, 'Holy Night: Demon Hunters', 'Action', 92, 'When a devil-worshipping criminal network plunges Seoul into chaos, the police turn to Holy Night—a trio of supernatural demon hunters—to restore order and defeat the rising evil.', 'https://image.tmdb.org/t/p/w500/v3Mo77Qjp6pctpD4eJaNT6kFRSB.jpg', '2025-04-30', 4.8),
(122, 'Tin Soldier', 'Action', 87, 'An ex-special forces operative seeks revenge against a cult leader who has corrupted his former comrades, the Shinjas. This leader, known as The Bokushi, promises veterans a purpose and protection, but is revealed to be a destructive influence. The ex-soldier, Nash Cavanaugh, joins forces with military operative Emmanuel Ashburn to infiltrate the Bokushi\'s fortress and expose his reign of terror', 'https://image.tmdb.org/t/p/w500/lFFDrFLXywFhy6khHes1LCFVMsL.jpg', '2025-05-22', 5.5),
(123, 'Brave Citizen', 'Action', 112, 'An expelled boxing champion, who now is a high-school teacher, witnesses intolerable violence and throws her first punch to build justice against it, while putting on a mask.', 'https://image.tmdb.org/t/p/w500/6Ea5i6APeTfm4hHh6dg5Z733JVS.jpg', '2023-10-25', 7.1),
(124, 'Conjuring the Cult', 'Horror', 93, 'After discovering his blood-soaked daughter dead in the bathtub, David Bryson attends a self-help group to help save him from his ghostly nightmares. But when a group of mysterious cult-like women offer to help him resurrect his daughter. David\'s choices will not just decide his fate... but the fate of his dead daughter\'s SOUL.', 'https://image.tmdb.org/t/p/w500/t4MiAeYpjL7saYvqvcn9xtOfA4K.jpg', '2024-10-01', 5.5),
(125, 'The Great Escape', 'Action', 90, '', 'https://image.tmdb.org/t/p/w500/h7shL668vhC2wsZQSBWzxkMuZ8K.jpg', '2023-05-26', 0.0),
(126, 'Captain America: Brave New World', 'Action', 119, 'After meeting with newly elected U.S. President Thaddeus Ross, Sam finds himself in the middle of an international incident. He must discover the reason behind a nefarious global plot before the true mastermind has the entire world seeing red.', 'https://image.tmdb.org/t/p/w500/pzIddUEMWhWzfvLI3TwxUG2wGoi.jpg', '2025-02-12', 6.1),
(127, 'Bad Influence', 'Thriller', 106, 'An ex-con gets a fresh start when hired to protect a wealthy heiress from a stalker — but their chemistry is hard to resist as they grow closer.', 'https://image.tmdb.org/t/p/w500/ghhooCOqQDqC6vhS1SVN2tCE0k8.jpg', '2025-01-24', 5.6),
(128, 'Stream', 'Horror', 84, 'Craven, a streamer with thousands of followers on a live streaming platform, has prepared a very special stream for Halloween. What no one expects when reacting to a video of Pentagram, a group of young paranormal investigators, is that the live experience will turn into the worst night of their lives. And maybe... the last.', 'https://image.tmdb.org/t/p/w500/nnyjtBfUYA8ASHA9OhADrX0sMNQ.jpg', '2024-02-06', 6.5),
(129, 'The Haunting at Saint Joseph\'s', 'Horror', 100, 'An engaged Muslim doctor, her fiancé, and their friends go on a holiday at St. Joseph’s Guesthouse, unaware that it was the site of a sacrifice of an innocent centuries before. They soon come to believe that it\'s haunted and causing them to go crazy, as their emotions spiral out of control.', 'https://image.tmdb.org/t/p/w500/eDzQFxs0KTzHUPkIR0c44TSGJUR.jpg', '2023-02-26', 3.8),
(130, 'Laila', 'Comedy', 134, 'Sonu Model, a renowned beautician from the old city, is forced to disguise himself as Laila, leading to a series of comedic, romantic, and action-packed events. Chaos ensues in this hilarious laugh riot', 'https://image.tmdb.org/t/p/w500/l4gsNxFPGpzbq0D6QK1a8vO1lBz.jpg', '2025-02-14', 5.0),
(131, 'Moana 2', 'Animation', 100, 'After receiving an unexpected call from her wayfinding ancestors, Moana journeys alongside Maui and a new crew to the far seas of Oceania and into dangerous, long-lost waters for an adventure unlike anything she\'s ever faced.', 'https://image.tmdb.org/t/p/w500/aLVkiINlIeCkcZIzb7XHzPYgO6L.jpg', '2024-11-21', 7.1);

-- --------------------------------------------------------

--
-- Table structure for table `screen`
--

CREATE TABLE `screen` (
  `screen_id` int(11) NOT NULL,
  `screen_name` varchar(50) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `screen`
--

INSERT INTO `screen` (`screen_id`, `screen_name`, `capacity`) VALUES
(1, 'Studio 1', 150),
(2, 'Studio 2', 150),
(3, 'Studio 3', 150);

-- --------------------------------------------------------

--
-- Table structure for table `screening_schedule`
--

CREATE TABLE `screening_schedule` (
  `schedule_id` int(11) NOT NULL,
  `film_id` int(11) NOT NULL,
  `screen_id` int(11) NOT NULL,
  `screening_date` date NOT NULL,
  `screening_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `screening_schedule`
--

INSERT INTO `screening_schedule` (`schedule_id`, `film_id`, `screen_id`, `screening_date`, `screening_time`) VALUES
(5, 61, 1, '2025-05-20', '10:00:00'),
(6, 13, 1, '2025-05-21', '10:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `screening_seat`
--

CREATE TABLE `screening_seat` (
  `screening_seat_id` int(11) NOT NULL,
  `schedule_id` int(11) NOT NULL,
  `screen_id` int(11) NOT NULL,
  `row_letter` char(1) NOT NULL,
  `seat_number` int(11) NOT NULL,
  `status` varchar(20) DEFAULT 'available',
  `price` decimal(10,2) NOT NULL DEFAULT 50.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `screening_seat`
--

INSERT INTO `screening_seat` (`screening_seat_id`, `schedule_id`, `screen_id`, `row_letter`, `seat_number`, `status`, `price`) VALUES
(1, 5, 1, 'A', 1, 'available', 50.00),
(2, 5, 1, 'A', 2, 'available', 50.00),
(3, 5, 1, 'A', 3, 'available', 50.00),
(4, 5, 1, 'A', 4, 'available', 50.00),
(5, 5, 1, 'A', 5, 'available', 50.00),
(6, 5, 1, 'A', 6, 'available', 50.00),
(7, 5, 1, 'A', 7, 'available', 50.00),
(8, 5, 1, 'A', 8, 'available', 50.00),
(9, 5, 1, 'A', 9, 'available', 50.00),
(10, 5, 1, 'A', 10, 'available', 50.00),
(11, 5, 1, 'A', 11, 'available', 50.00),
(12, 5, 1, 'A', 12, 'available', 50.00),
(13, 5, 1, 'A', 13, 'available', 50.00),
(14, 5, 1, 'A', 14, 'available', 50.00),
(15, 5, 1, 'A', 15, 'available', 50.00),
(16, 5, 1, 'B', 1, 'available', 50.00),
(17, 5, 1, 'B', 2, 'available', 50.00),
(18, 5, 1, 'B', 3, 'available', 50.00),
(19, 5, 1, 'B', 4, 'available', 50.00),
(20, 5, 1, 'B', 5, 'available', 50.00),
(21, 5, 1, 'B', 6, 'available', 50.00),
(22, 5, 1, 'B', 7, 'available', 50.00),
(23, 5, 1, 'B', 8, 'available', 50.00),
(24, 5, 1, 'B', 9, 'available', 50.00),
(25, 5, 1, 'B', 10, 'available', 50.00),
(26, 5, 1, 'B', 11, 'available', 50.00),
(27, 5, 1, 'B', 12, 'available', 50.00),
(28, 5, 1, 'B', 13, 'available', 50.00),
(29, 5, 1, 'B', 14, 'available', 50.00),
(30, 5, 1, 'B', 15, 'available', 50.00),
(31, 5, 1, 'C', 1, 'available', 50.00),
(32, 5, 1, 'C', 2, 'available', 50.00),
(33, 5, 1, 'C', 3, 'available', 50.00),
(34, 5, 1, 'C', 4, 'available', 50.00),
(35, 5, 1, 'C', 5, 'available', 50.00),
(36, 5, 1, 'C', 6, 'available', 50.00),
(37, 5, 1, 'C', 7, 'available', 50.00),
(38, 5, 1, 'C', 8, 'available', 50.00),
(39, 5, 1, 'C', 9, 'available', 50.00),
(40, 5, 1, 'C', 10, 'available', 50.00),
(41, 5, 1, 'C', 11, 'available', 50.00),
(42, 5, 1, 'C', 12, 'available', 50.00),
(43, 5, 1, 'C', 13, 'available', 50.00),
(44, 5, 1, 'C', 14, 'available', 50.00),
(45, 5, 1, 'C', 15, 'available', 50.00),
(46, 5, 1, 'D', 1, 'available', 50.00),
(47, 5, 1, 'D', 2, 'available', 50.00),
(48, 5, 1, 'D', 3, 'available', 50.00),
(49, 5, 1, 'D', 4, 'available', 50.00),
(50, 5, 1, 'D', 5, 'available', 50.00),
(51, 5, 1, 'D', 6, 'available', 50.00),
(52, 5, 1, 'D', 7, 'available', 50.00),
(53, 5, 1, 'D', 8, 'available', 50.00),
(54, 5, 1, 'D', 9, 'available', 50.00),
(55, 5, 1, 'D', 10, 'available', 50.00),
(56, 5, 1, 'D', 11, 'available', 50.00),
(57, 5, 1, 'D', 12, 'available', 50.00),
(58, 5, 1, 'D', 13, 'available', 50.00),
(59, 5, 1, 'D', 14, 'available', 50.00),
(60, 5, 1, 'D', 15, 'available', 50.00),
(61, 5, 1, 'E', 1, 'available', 50.00),
(62, 5, 1, 'E', 2, 'available', 50.00),
(63, 5, 1, 'E', 3, 'available', 50.00),
(64, 5, 1, 'E', 4, 'available', 50.00),
(65, 5, 1, 'E', 5, 'available', 50.00),
(66, 5, 1, 'E', 6, 'available', 50.00),
(67, 5, 1, 'E', 7, 'available', 50.00),
(68, 5, 1, 'E', 8, 'available', 50.00),
(69, 5, 1, 'E', 9, 'available', 50.00),
(70, 5, 1, 'E', 10, 'available', 50.00),
(71, 5, 1, 'E', 11, 'available', 50.00),
(72, 5, 1, 'E', 12, 'available', 50.00),
(73, 5, 1, 'E', 13, 'available', 50.00),
(74, 5, 1, 'E', 14, 'available', 50.00),
(75, 5, 1, 'E', 15, 'available', 50.00),
(76, 5, 1, 'F', 1, 'available', 50.00),
(77, 5, 1, 'F', 2, 'available', 50.00),
(78, 5, 1, 'F', 3, 'available', 50.00),
(79, 5, 1, 'F', 4, 'available', 50.00),
(80, 5, 1, 'F', 5, 'available', 50.00),
(81, 5, 1, 'F', 6, 'available', 50.00),
(82, 5, 1, 'F', 7, 'available', 50.00),
(83, 5, 1, 'F', 8, 'available', 50.00),
(84, 5, 1, 'F', 9, 'available', 50.00),
(85, 5, 1, 'F', 10, 'available', 50.00),
(86, 5, 1, 'F', 11, 'available', 50.00),
(87, 5, 1, 'F', 12, 'available', 50.00),
(88, 5, 1, 'F', 13, 'available', 50.00),
(89, 5, 1, 'F', 14, 'available', 50.00),
(90, 5, 1, 'F', 15, 'available', 50.00),
(91, 5, 1, 'G', 1, 'available', 50.00),
(92, 5, 1, 'G', 2, 'available', 50.00),
(93, 5, 1, 'G', 3, 'available', 50.00),
(94, 5, 1, 'G', 4, 'available', 50.00),
(95, 5, 1, 'G', 5, 'available', 50.00),
(96, 5, 1, 'G', 6, 'available', 50.00),
(97, 5, 1, 'G', 7, 'available', 50.00),
(98, 5, 1, 'G', 8, 'available', 50.00),
(99, 5, 1, 'G', 9, 'available', 50.00),
(100, 5, 1, 'G', 10, 'available', 50.00),
(101, 5, 1, 'G', 11, 'available', 50.00),
(102, 5, 1, 'G', 12, 'available', 50.00),
(103, 5, 1, 'G', 13, 'available', 50.00),
(104, 5, 1, 'G', 14, 'available', 50.00),
(105, 5, 1, 'G', 15, 'available', 50.00),
(106, 5, 1, 'H', 1, 'available', 50.00),
(107, 5, 1, 'H', 2, 'available', 50.00),
(108, 5, 1, 'H', 3, 'available', 50.00),
(109, 5, 1, 'H', 4, 'available', 50.00),
(110, 5, 1, 'H', 5, 'available', 50.00),
(111, 5, 1, 'H', 6, 'available', 50.00),
(112, 5, 1, 'H', 7, 'available', 50.00),
(113, 5, 1, 'H', 8, 'available', 50.00),
(114, 5, 1, 'H', 9, 'available', 50.00),
(115, 5, 1, 'H', 10, 'available', 50.00),
(116, 5, 1, 'H', 11, 'available', 50.00),
(117, 5, 1, 'H', 12, 'available', 50.00),
(118, 5, 1, 'H', 13, 'available', 50.00),
(119, 5, 1, 'H', 14, 'available', 50.00),
(120, 5, 1, 'H', 15, 'available', 50.00),
(121, 5, 1, 'I', 1, 'available', 50.00),
(122, 5, 1, 'I', 2, 'available', 50.00),
(123, 5, 1, 'I', 3, 'available', 50.00),
(124, 5, 1, 'I', 4, 'available', 50.00),
(125, 5, 1, 'I', 5, 'available', 50.00),
(126, 5, 1, 'I', 6, 'available', 50.00),
(127, 5, 1, 'I', 7, 'available', 50.00),
(128, 5, 1, 'I', 8, 'available', 50.00),
(129, 5, 1, 'I', 9, 'available', 50.00),
(130, 5, 1, 'I', 10, 'available', 50.00),
(131, 5, 1, 'I', 11, 'available', 50.00),
(132, 5, 1, 'I', 12, 'available', 50.00),
(133, 5, 1, 'I', 13, 'available', 50.00),
(134, 5, 1, 'I', 14, 'available', 50.00),
(135, 5, 1, 'I', 15, 'available', 50.00),
(136, 5, 1, 'J', 1, 'available', 50.00),
(137, 5, 1, 'J', 2, 'available', 50.00),
(138, 5, 1, 'J', 3, 'available', 50.00),
(139, 5, 1, 'J', 4, 'available', 50.00),
(140, 5, 1, 'J', 5, 'available', 50.00),
(141, 5, 1, 'J', 6, 'available', 50.00),
(142, 5, 1, 'J', 7, 'available', 50.00),
(143, 5, 1, 'J', 8, 'available', 50.00),
(144, 5, 1, 'J', 9, 'available', 50.00),
(145, 5, 1, 'J', 10, 'available', 50.00),
(146, 5, 1, 'J', 11, 'available', 50.00),
(147, 5, 1, 'J', 12, 'available', 50.00),
(148, 5, 1, 'J', 13, 'available', 50.00),
(149, 5, 1, 'J', 14, 'available', 50.00),
(150, 5, 1, 'J', 15, 'available', 50.00),
(151, 6, 1, 'A', 1, 'available', 50.00),
(152, 6, 1, 'A', 2, 'available', 50.00),
(153, 6, 1, 'A', 3, 'available', 50.00),
(154, 6, 1, 'A', 4, 'available', 50.00),
(155, 6, 1, 'A', 5, 'available', 50.00),
(156, 6, 1, 'A', 6, 'available', 50.00),
(157, 6, 1, 'A', 7, 'available', 50.00),
(158, 6, 1, 'A', 8, 'available', 50.00),
(159, 6, 1, 'A', 9, 'available', 50.00),
(160, 6, 1, 'A', 10, 'available', 50.00),
(161, 6, 1, 'A', 11, 'available', 50.00),
(162, 6, 1, 'A', 12, 'available', 50.00),
(163, 6, 1, 'A', 13, 'available', 50.00),
(164, 6, 1, 'A', 14, 'available', 50.00),
(165, 6, 1, 'A', 15, 'available', 50.00),
(166, 6, 1, 'B', 1, 'available', 50.00),
(167, 6, 1, 'B', 2, 'available', 50.00),
(168, 6, 1, 'B', 3, 'available', 50.00),
(169, 6, 1, 'B', 4, 'available', 50.00),
(170, 6, 1, 'B', 5, 'available', 50.00),
(171, 6, 1, 'B', 6, 'available', 50.00),
(172, 6, 1, 'B', 7, 'available', 50.00),
(173, 6, 1, 'B', 8, 'available', 50.00),
(174, 6, 1, 'B', 9, 'available', 50.00),
(175, 6, 1, 'B', 10, 'available', 50.00),
(176, 6, 1, 'B', 11, 'available', 50.00),
(177, 6, 1, 'B', 12, 'available', 50.00),
(178, 6, 1, 'B', 13, 'available', 50.00),
(179, 6, 1, 'B', 14, 'available', 50.00),
(180, 6, 1, 'B', 15, 'available', 50.00),
(181, 6, 1, 'C', 1, 'available', 50.00),
(182, 6, 1, 'C', 2, 'available', 50.00),
(183, 6, 1, 'C', 3, 'available', 50.00),
(184, 6, 1, 'C', 4, 'available', 50.00),
(185, 6, 1, 'C', 5, 'available', 50.00),
(186, 6, 1, 'C', 6, 'available', 50.00),
(187, 6, 1, 'C', 7, 'available', 50.00),
(188, 6, 1, 'C', 8, 'available', 50.00),
(189, 6, 1, 'C', 9, 'available', 50.00),
(190, 6, 1, 'C', 10, 'available', 50.00),
(191, 6, 1, 'C', 11, 'available', 50.00),
(192, 6, 1, 'C', 12, 'available', 50.00),
(193, 6, 1, 'C', 13, 'available', 50.00),
(194, 6, 1, 'C', 14, 'available', 50.00),
(195, 6, 1, 'C', 15, 'available', 50.00),
(196, 6, 1, 'D', 1, 'available', 50.00),
(197, 6, 1, 'D', 2, 'available', 50.00),
(198, 6, 1, 'D', 3, 'available', 50.00),
(199, 6, 1, 'D', 4, 'available', 50.00),
(200, 6, 1, 'D', 5, 'available', 50.00),
(201, 6, 1, 'D', 6, 'available', 50.00),
(202, 6, 1, 'D', 7, 'available', 50.00),
(203, 6, 1, 'D', 8, 'available', 50.00),
(204, 6, 1, 'D', 9, 'available', 50.00),
(205, 6, 1, 'D', 10, 'available', 50.00),
(206, 6, 1, 'D', 11, 'available', 50.00),
(207, 6, 1, 'D', 12, 'available', 50.00),
(208, 6, 1, 'D', 13, 'available', 50.00),
(209, 6, 1, 'D', 14, 'available', 50.00),
(210, 6, 1, 'D', 15, 'available', 50.00),
(211, 6, 1, 'E', 1, 'available', 50.00),
(212, 6, 1, 'E', 2, 'available', 50.00),
(213, 6, 1, 'E', 3, 'available', 50.00),
(214, 6, 1, 'E', 4, 'available', 50.00),
(215, 6, 1, 'E', 5, 'available', 50.00),
(216, 6, 1, 'E', 6, 'available', 50.00),
(217, 6, 1, 'E', 7, 'available', 50.00),
(218, 6, 1, 'E', 8, 'available', 50.00),
(219, 6, 1, 'E', 9, 'available', 50.00),
(220, 6, 1, 'E', 10, 'available', 50.00),
(221, 6, 1, 'E', 11, 'available', 50.00),
(222, 6, 1, 'E', 12, 'available', 50.00),
(223, 6, 1, 'E', 13, 'available', 50.00),
(224, 6, 1, 'E', 14, 'available', 50.00),
(225, 6, 1, 'E', 15, 'available', 50.00),
(226, 6, 1, 'F', 1, 'available', 50.00),
(227, 6, 1, 'F', 2, 'available', 50.00),
(228, 6, 1, 'F', 3, 'available', 50.00),
(229, 6, 1, 'F', 4, 'available', 50.00),
(230, 6, 1, 'F', 5, 'available', 50.00),
(231, 6, 1, 'F', 6, 'available', 50.00),
(232, 6, 1, 'F', 7, 'available', 50.00),
(233, 6, 1, 'F', 8, 'available', 50.00),
(234, 6, 1, 'F', 9, 'available', 50.00),
(235, 6, 1, 'F', 10, 'available', 50.00),
(236, 6, 1, 'F', 11, 'available', 50.00),
(237, 6, 1, 'F', 12, 'available', 50.00),
(238, 6, 1, 'F', 13, 'available', 50.00),
(239, 6, 1, 'F', 14, 'available', 50.00),
(240, 6, 1, 'F', 15, 'available', 50.00),
(241, 6, 1, 'G', 1, 'available', 50.00),
(242, 6, 1, 'G', 2, 'available', 50.00),
(243, 6, 1, 'G', 3, 'available', 50.00),
(244, 6, 1, 'G', 4, 'available', 50.00),
(245, 6, 1, 'G', 5, 'available', 50.00),
(246, 6, 1, 'G', 6, 'available', 50.00),
(247, 6, 1, 'G', 7, 'available', 50.00),
(248, 6, 1, 'G', 8, 'available', 50.00),
(249, 6, 1, 'G', 9, 'available', 50.00),
(250, 6, 1, 'G', 10, 'available', 50.00),
(251, 6, 1, 'G', 11, 'available', 50.00),
(252, 6, 1, 'G', 12, 'available', 50.00),
(253, 6, 1, 'G', 13, 'available', 50.00),
(254, 6, 1, 'G', 14, 'available', 50.00),
(255, 6, 1, 'G', 15, 'available', 50.00),
(256, 6, 1, 'H', 1, 'available', 50.00),
(257, 6, 1, 'H', 2, 'available', 50.00),
(258, 6, 1, 'H', 3, 'available', 50.00),
(259, 6, 1, 'H', 4, 'available', 50.00),
(260, 6, 1, 'H', 5, 'available', 50.00),
(261, 6, 1, 'H', 6, 'available', 50.00),
(262, 6, 1, 'H', 7, 'available', 50.00),
(263, 6, 1, 'H', 8, 'available', 50.00),
(264, 6, 1, 'H', 9, 'available', 50.00),
(265, 6, 1, 'H', 10, 'available', 50.00),
(266, 6, 1, 'H', 11, 'available', 50.00),
(267, 6, 1, 'H', 12, 'available', 50.00),
(268, 6, 1, 'H', 13, 'available', 50.00),
(269, 6, 1, 'H', 14, 'available', 50.00),
(270, 6, 1, 'H', 15, 'available', 50.00),
(271, 6, 1, 'I', 1, 'available', 50.00),
(272, 6, 1, 'I', 2, 'available', 50.00),
(273, 6, 1, 'I', 3, 'available', 50.00),
(274, 6, 1, 'I', 4, 'available', 50.00),
(275, 6, 1, 'I', 5, 'available', 50.00),
(276, 6, 1, 'I', 6, 'available', 50.00),
(277, 6, 1, 'I', 7, 'available', 50.00),
(278, 6, 1, 'I', 8, 'available', 50.00),
(279, 6, 1, 'I', 9, 'available', 50.00),
(280, 6, 1, 'I', 10, 'available', 50.00),
(281, 6, 1, 'I', 11, 'available', 50.00),
(282, 6, 1, 'I', 12, 'available', 50.00),
(283, 6, 1, 'I', 13, 'available', 50.00),
(284, 6, 1, 'I', 14, 'available', 50.00),
(285, 6, 1, 'I', 15, 'available', 50.00),
(286, 6, 1, 'J', 1, 'available', 50.00),
(287, 6, 1, 'J', 2, 'available', 50.00),
(288, 6, 1, 'J', 3, 'available', 50.00),
(289, 6, 1, 'J', 4, 'available', 50.00),
(290, 6, 1, 'J', 5, 'available', 50.00),
(291, 6, 1, 'J', 6, 'available', 50.00),
(292, 6, 1, 'J', 7, 'available', 50.00),
(293, 6, 1, 'J', 8, 'available', 50.00),
(294, 6, 1, 'J', 9, 'available', 50.00),
(295, 6, 1, 'J', 10, 'available', 50.00),
(296, 6, 1, 'J', 11, 'available', 50.00),
(297, 6, 1, 'J', 12, 'available', 50.00),
(298, 6, 1, 'J', 13, 'available', 50.00),
(299, 6, 1, 'J', 14, 'available', 50.00),
(300, 6, 1, 'J', 15, 'available', 50.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Indexes for table `booking_seat`
--
ALTER TABLE `booking_seat`
  ADD PRIMARY KEY (`booking_id`,`screening_seat_id`),
  ADD KEY `screening_seat_id` (`screening_seat_id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `film`
--
ALTER TABLE `film`
  ADD PRIMARY KEY (`film_id`);

--
-- Indexes for table `screen`
--
ALTER TABLE `screen`
  ADD PRIMARY KEY (`screen_id`);

--
-- Indexes for table `screening_schedule`
--
ALTER TABLE `screening_schedule`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `screening_date` (`screening_date`,`screening_time`),
  ADD KEY `fk_screening_film` (`film_id`),
  ADD KEY `fk_screening_screen` (`screen_id`);

--
-- Indexes for table `screening_seat`
--
ALTER TABLE `screening_seat`
  ADD PRIMARY KEY (`screening_seat_id`),
  ADD UNIQUE KEY `unique_seat_per_screening` (`schedule_id`,`row_letter`,`seat_number`),
  ADD KEY `screen_id` (`screen_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `film`
--
ALTER TABLE `film`
  MODIFY `film_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=132;

--
-- AUTO_INCREMENT for table `screen`
--
ALTER TABLE `screen`
  MODIFY `screen_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `screening_schedule`
--
ALTER TABLE `screening_schedule`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `screening_seat`
--
ALTER TABLE `screening_seat`
  MODIFY `screening_seat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=301;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `booking_ibfk_3` FOREIGN KEY (`schedule_id`) REFERENCES `screening_schedule` (`schedule_id`);

--
-- Constraints for table `booking_seat`
--
ALTER TABLE `booking_seat`
  ADD CONSTRAINT `booking_seat_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `booking_seat_ibfk_2` FOREIGN KEY (`screening_seat_id`) REFERENCES `screening_seat` (`screening_seat_id`) ON DELETE CASCADE;

--
-- Constraints for table `screening_schedule`
--
ALTER TABLE `screening_schedule`
  ADD CONSTRAINT `fk_screening_film` FOREIGN KEY (`film_id`) REFERENCES `film` (`film_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_screening_screen` FOREIGN KEY (`screen_id`) REFERENCES `screen` (`screen_id`) ON DELETE CASCADE;

--
-- Constraints for table `screening_seat`
--
ALTER TABLE `screening_seat`
  ADD CONSTRAINT `screening_seat_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `screening_schedule` (`schedule_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `screening_seat_ibfk_2` FOREIGN KEY (`screen_id`) REFERENCES `screen` (`screen_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
