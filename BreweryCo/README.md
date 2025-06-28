# Documentation du test technique

## Objectif

Modifier une application existante pour ajouter une fonctionnalité.

## Récits à effectuer

### Recit 1 : Corriger le bug
Le client remarque qu'il manque des brasseries dans la liste. Il faut corriger le bug pour que toutes les brasseries soient affichées. 
Présentement nous affichons seulement les brasseries de la page 72 brasseries alors qu'il devrai en avoir 100.

### Recit 2 : Gestion des états
Le client souhaite que l'application gère les états de chargement et d'erreur lors de la récupération des brasseries.
Il faut ajouter une gestion des états pour afficher un indicateur de chargement pendant la récupération des données et un message d'erreur en cas de problème.

### Recit 3 : Ajouter un écran de détail
Le client souhaite que l'utilisateur puisse cliquer sur une brasserie pour voir plus de détails. Il faut créer un écran de détail qui affiche les informations d'une brasserie sélectionnée.
Le détail doit inclure au minimum :
- Le nom de la brasserie
- L'adresse
- Le type de brasserie
- Le site web
- Le numéro de téléphone

## Contraintes

- Langage : Kotlin
- UI : Jetpack Compose
- Tests : JUnit5, AssertJ

## Livrable

- Un lien vers votre dépôt GitHub ou GitLab

## Processus

- Commencez par un commit initial avec le code copié.
- Faites un commit pour chaque tâche, en utilisant le message : `[RECIT-{{numéro}}] {{description de la tâche}}`.
- Il n’est pas attendu que toutes les tâches soient terminées. Donnez-vous un maximum de 3h puis livrez votre travail.

---

Bonne chance !