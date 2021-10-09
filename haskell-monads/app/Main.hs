module Main where

import Control.Monad (forM_)

data Expression
  = Const Int
  | Div Expression Expression
  deriving (Show)

data DivisionByZero = DivisionByZero

instance Show DivisionByZero where
  show _ = "Division by zero"

-- Haskell already defines a generic monad with unit (called return in Haskell) and bind (the strange >>= operator)

-- do-notation is the way to write monadic comprehensions in Haskell!
eval :: Expression -> Either DivisionByZero Int
eval (Const num) = return num
eval (Div a b) = do
  x <- eval a
  y <- eval b
  x `safeDiv` y

-- Monadic bind alternative to the code above
--eval (Div a b) =
--  eval a >>= (\x -> eval b >>= (\y -> x `safeDiv` y))

safeDiv :: Int -> Int -> Either DivisionByZero Int
safeDiv _ 0 = Left DivisionByZero
safeDiv x y = return $ x `div` y

-- List monad
-- unlike in Kotlin, it is possible to use a monadic comprehension for non-deterministic
-- structures such as lists and sequences as well
cartesian :: [a] -> [b] -> [(a, b)]
--cartesian xs ys = xs >>= (\x -> ys >>= (\y -> return (x, y)))
cartesian xs ys = do
  x <- xs
  y <- ys
  return (x, y)

-- IO monad used for printing to STDOUT
main :: IO ()
main = do
  let answer = Div (Div (Const 1932) (Const 23)) (Const 2)
  let err = Div (Const 1) (Const 0)
  let complexError = Div answer err
  forM_ (eval <$> [answer, err, complexError]) print
