-- Seed 3 Administrator Accounts (Password is 'admin123' for all three)
INSERT INTO admins (admin_id, name, email, mobile_number, password_hash, is_active, role)
SELECT 'ADMIN001', 'Super Administrator', 'admin001@getfeetwithus.com', '9876543210', '$2b$10$YHMnQxN25/0hHQjlZlQDXOtjBFqCpwh48MTuinoO45Vm.Ke2Iaxde', TRUE, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM admins WHERE admin_id = 'ADMIN001');

INSERT INTO admins (admin_id, name, email, mobile_number, password_hash, is_active, role)
SELECT 'ADMIN002', 'Fitness & Diet Lead', 'admin002@getfeetwithus.com', '9876543211', '$2b$10$YHMnQxN25/0hHQjlZlQDXOtjBFqCpwh48MTuinoO45Vm.Ke2Iaxde', TRUE, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM admins WHERE admin_id = 'ADMIN002');

INSERT INTO admins (admin_id, name, email, mobile_number, password_hash, is_active, role)
SELECT 'ADMIN003', 'Operations & Sales Lead', 'admin003@getfeetwithus.com', '9876543212', '$2b$10$YHMnQxN25/0hHQjlZlQDXOtjBFqCpwh48MTuinoO45Vm.Ke2Iaxde', TRUE, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM admins WHERE admin_id = 'ADMIN003');

-- Seed Organization Details
INSERT INTO organization_details (id, org_name, logo_url, about_info, address, mobile_number, email, website, other_info)
SELECT 1, 'Get Feet With Us', '', 'Get Feet With Us is a dedicated health, fitness, diet, and meeting platform designed to empower individuals with sustainable everyday wellness routines, expert nutritional diet plans, and holistic yoga practices.', '100 Fitness Boulevard, Health District, Bhubaneswar, Odisha, 751024', '+91 9876543210', 'support@getfeetwithus.com', 'https://www.getfeetwithus.com', 'Leading Community Health & Wellness Network.'
WHERE NOT EXISTS (SELECT 1 FROM organization_details WHERE id = 1);

-- Seed Sample Today Diet Plan
INSERT INTO diet_master (diet_id, diet_date, title, description, tips, instructions, image_url, video_url, other_instructions, is_active, created_by)
SELECT 'DIET101', CURRENT_DATE(), 'High-Protein Metabolic Cleanse Diet', 'A nutrient-dense whole foods diet formulated to support muscle recovery, lean metabolic rate, and optimal digestion.', 'Drink at least 3.5 liters of filtered warm water throughout the day. Avoid refined sugars after 6 PM.', 'Breakfast: Sprouted moong salad, 2 boiled egg whites / tofu, green tea with lemon.\nLunch: 1 cup brown rice / 2 multigrain rotis, mixed dal, grilled paneer/chicken, cucumber salad.\nEvening Snack: Roasted makhana / handful of almonds and walnuts.\nDinner: Clear vegetable soup with steamed greens and boiled lentils.', '', 'https://www.youtube.com', 'Adjust portion sizes based on individual caloric requirement. Consult our dietitian for customizations.', TRUE, 'ADMIN001'
WHERE NOT EXISTS (SELECT 1 FROM diet_master WHERE diet_id = 'DIET101');

-- Seed Sample Fitness & Yoga Activities
INSERT INTO fitness_master (fitness_id, title, description, instructions, image_url, video_url, duration, repetitions, other_instructions, category, is_active)
SELECT 'FIT101', 'Morning Surya Namaskar (Sun Salutations)', 'Classic 12-pose sequence to awaken whole-body flexibility, improve respiratory rhythm, and energize morning focus.', '1. Pranamasana (Prayer pose)\n2. Hastauttanasana (Raised arms pose)\n3. Hastapadasana (Standing forward bend)\n4. Ashwa Sanchalanasana (Equestrian pose)\n5. Dandasana (Plank pose)\n6. Ashtanga Namaskara (Salute with eight parts)\n7. Bhujangasana (Cobra pose)\n8. Adho Mukha Svanasana (Downward-facing dog)\n9. Ashwa Sanchalanasana (Equestrian pose)\n10. Hastapadasana (Standing forward bend)\n11. Hastauttanasana (Raised arms)\n12. Tadasana (Mountain pose)', '', 'https://www.youtube.com', '20 Mins', '6 Full Rounds', 'Practice on an empty stomach with a standard non-slip yoga mat.', 'Yoga', TRUE
WHERE NOT EXISTS (SELECT 1 FROM fitness_master WHERE fitness_id = 'FIT101');

INSERT INTO fitness_master (fitness_id, title, description, instructions, image_url, video_url, duration, repetitions, other_instructions, category, is_active)
SELECT 'FIT102', 'Core Strength & Functional Mobility', 'Low-impact core conditioning for spinal support, abdominal strength, and improved posture.', '1. Forearm Plank: Hold 45s\n2. Bird-Dog: 12 reps per side\n3. Glute Bridges: 15 reps\n4. Dead Bugs: 12 reps per side', '', '', '15 Mins', '3 Sets', 'Keep core engaged and maintain neutral spine throughout.', 'Strength', TRUE
WHERE NOT EXISTS (SELECT 1 FROM fitness_master WHERE fitness_id = 'FIT102');

INSERT INTO fitness_master (fitness_id, title, description, instructions, image_url, video_url, duration, repetitions, other_instructions, category, is_active)
SELECT 'FIT103', 'Evening Gentle Yin Yoga & Stress Release', 'Restorative slow stretches to decompress hips, hamstrings, and release nervous tension before bedtime.', 'Hold Child Pose, Reclined Butterfly, and Pigeon Pose for 2-3 minutes each with deep rhythmic diaphragmatic breathing.', '', '', '25 Mins', '1 Cycle', 'Dim room lights and avoid digital screens during practice.', 'Yoga', TRUE
WHERE NOT EXISTS (SELECT 1 FROM fitness_master WHERE fitness_id = 'FIT103');

-- Seed Sample Products
INSERT INTO product_master (product_id, name, short_description, description, price, is_available, image_url, why_to_use, who_can_use, how_to_use, other_instructions, is_active)
SELECT 'PRD101', 'Organic Herbal Detox Green Tea', 'Premium antioxidant blend with moringa, tulsi, and lemongrass.', 'Our Organic Herbal Detox Green Tea is hand-harvested from certified organic gardens. Formulated to enhance metabolic rate, clear skin toxins, and provide gentle daily rejuvenation.', 499.00, TRUE, '', 'Helps boost daily metabolic energy, detoxifies digestive tract, and reduces oxidative stress.', 'Adults aged 16+ seeking healthy herbal beverages without artificial additives.', 'Steep 1 tea bag in 200ml boiling water for 3-4 minutes. Enjoy warm twice a day before meals.', 'Store in an airtight container in a dry location away from direct sunlight.', TRUE
WHERE NOT EXISTS (SELECT 1 FROM product_master WHERE product_id = 'PRD101');

INSERT INTO product_master (product_id, name, short_description, description, price, is_available, image_url, why_to_use, who_can_use, how_to_use, other_instructions, is_active)
SELECT 'PRD102', 'Raw Chia & Roasted Flax Seeds Mix', 'Rich source of Omega-3 fatty acids, dietary fiber, and plant protein.', 'A potent superfood blend of raw organic black chia seeds and slow-roasted golden flax seeds. Essential for heart health and digestive regularity.', 349.00, TRUE, '', 'Provides essential Omega-3 fatty acids and soluble dietary fiber for gut health.', 'Ideal for individuals of all age groups, athletes, and fitness enthusiasts.', 'Sprinkle 1-2 tablespoons over oatmeal, smoothies, yogurt, or salads daily.', 'Refrigerate after opening to preserve nutrient freshness.', TRUE
WHERE NOT EXISTS (SELECT 1 FROM product_master WHERE product_id = 'PRD102');

INSERT INTO product_master (product_id, name, short_description, description, price, is_available, image_url, why_to_use, who_can_use, how_to_use, other_instructions, is_active)
SELECT 'PRD103', 'Natural Plant Protein Blend (Chocolate)', '25g clean plant protein with digestive enzymes and BCAA.', 'Formulated from organic pea, brown rice, and pumpkin seed proteins. Contains no added sugar, zero dairy, and no artificial sweeteners.', 1899.00, TRUE, '', 'Supports lean muscle repair, faster workout recovery, and daily protein targets.', 'Active individuals, bodybuilders, vegans, and lactose-intolerant members.', 'Mix 1 scoop (33g) with 300ml cold water or plant milk. Shake well and drink post-exercise.', 'Keep container tightly sealed in a dry environment.', TRUE
WHERE NOT EXISTS (SELECT 1 FROM product_master WHERE product_id = 'PRD103');

INSERT INTO product_master (product_id, name, short_description, description, price, is_available, image_url, why_to_use, who_can_use, how_to_use, other_instructions, is_active)
SELECT 'PRD104', 'Eco-Friendly Non-Slip Cork Yoga Mat', 'Natural cork and recycled rubber yoga mat with alignment guidelines.', 'High-traction sustainable cork surface that becomes grippier with sweat. Features laser-etched posture alignment guides.', 1450.00, TRUE, '', 'Ensures superior grip, joint cushioning, and posture alignment during yoga sessions.', 'Yoga practitioners of all experience levels.', 'Unroll on a flat surface. Wipe with a damp cloth after intense practice.', 'Do not fold; roll loosely with cork side facing outward.', TRUE
WHERE NOT EXISTS (SELECT 1 FROM product_master WHERE product_id = 'PRD104');

-- Seed Sample Today Live Meeting & Past Meeting
INSERT INTO meeting_master (meeting_id, topic, host_details, agenda, meeting_date, start_time, duration, meeting_mode, meeting_address, meeting_link, other_details, is_active, is_cancelled)
SELECT 'MTG101', 'Interactive Yoga Alignment & Breathing Workshop', 'Master Trainer Aditi & Dr. Sharma', 'Live interactive demonstration of pranayama breathing techniques, spinal posture corrections, and open Q&A on overcoming home workout plateaus.', CURRENT_DATE(), '18:30:00', '45 Mins', 'Online', '', 'https://meet.google.com/get-feet-live', 'Please wear comfortable exercise clothing and have a mat ready.', TRUE, FALSE
WHERE NOT EXISTS (SELECT 1 FROM meeting_master WHERE meeting_id = 'MTG101');

INSERT INTO meeting_master (meeting_id, topic, host_details, agenda, meeting_date, start_time, duration, meeting_mode, meeting_address, meeting_link, other_details, is_active, is_cancelled)
SELECT 'MTG100', 'Holistic Nutrition & Sugar Detox Seminar', 'Dr. Alok Verma (Chief Clinical Nutritionist)', 'Understanding glycemic indexes, hidden refined sugars in processed foods, and building an enjoyable whole-food diet routine.', DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '11:00:00', '60 Mins', 'Hybrid', 'Conference Hall A, Wellness Hub, Bhubaneswar', 'https://meet.google.com/sugar-detox-archive', 'Recorded session notes and dietary handout distributed.', TRUE, FALSE
WHERE NOT EXISTS (SELECT 1 FROM meeting_master WHERE meeting_id = 'MTG100');
